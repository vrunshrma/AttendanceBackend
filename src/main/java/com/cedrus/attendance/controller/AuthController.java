package com.cedrus.attendance.controller;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.cedrus.attendance.bean.OnRegistrationCompleteEvent;
import com.cedrus.attendance.bean.PasswordDTO;
import com.cedrus.attendance.bean.UserBean;
import com.cedrus.attendance.bean.UserRegistration;
import com.cedrus.attendance.common.BaseUrls;
import com.cedrus.attendance.common.Constants;
import com.cedrus.attendance.common.Messages;
import com.cedrus.attendance.entity.PasswordResetToken;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.entity.VerificationToken;
import com.cedrus.attendance.exceptions.UserAlreadyExistException;
import com.cedrus.attendance.exceptions.UserNotAutharizedException;
import com.cedrus.attendance.repository.PasswordResetTokenRepository;
import com.cedrus.attendance.repository.UserRepository;
import com.cedrus.attendance.request.LoginRequest;
import com.cedrus.attendance.response.APIResponse;
import com.cedrus.attendance.response.GenericResponse;
import com.cedrus.attendance.response.JwtResponse;
import com.cedrus.attendance.service.EmailService;
import com.cedrus.attendance.service.IAuthService;
import com.cedrus.attendance.service.IUserService;
import com.cedrus.attendance.service.SecurityService;
import com.cedrus.attendance.service.UserDetailService;
import com.cedrus.attendance.service.UserService;
import com.cedrus.attendance.utility.JwtUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(BaseUrls.AUTH_BASE_URL)
public class AuthController {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private IAuthService service;

	private static final String BEARER = "Bearer";

	@Autowired
	private IUserService iUserService;

	@Autowired
	private MessageSource messages;
	
	@Autowired
	SecurityService securityService;

	@Autowired
	UserDetailService userDetailService;

	@Autowired
	private Environment env;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	PasswordResetTokenRepository passwordResetTokenRepository;

	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@Autowired
	private JwtUtils jwtUtils;

	AuthenticationManager authenticationManager;

	@Value("${attendance-backend.frontend.url}")
	String appUrl;

	@PostMapping(BaseUrls.LOGIN)
	public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		log.info("AuthController :: user authenticate start : {} ", loginRequest);
		return ResponseEntity.ok(service.login(loginRequest));
	}

	@PostMapping(BaseUrls.CHECK_USERNAME)
	public ResponseEntity<APIResponse<UserBean>> chechByUsername(@RequestBody UserBean userBean) {
		APIResponse<UserBean> response = iUserService.findByEmailId(userBean);
		if (StringUtils.equals(response.getStatus(), Constants.SUCCESS.getValue()))
			return ResponseEntity.ok(response);
		return ResponseEntity.badRequest().body(response);
	}

	@PostMapping("/user/registration")
	public String registerUserAccount(@Valid @RequestBody UserRegistration userRegistration, final Errors errors) {
		LOGGER.debug("Registering user account with information: {}", userRegistration);

		try {
			final User registered = iUserService.registerNewUserAccount(userRegistration);
			final String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
			Locale locale = LocaleContextHolder.getLocale();
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, locale, appUrl));
		} catch (final UserAlreadyExistException uaeEx) {
			String errMessage = messages.getMessage("message.regError", null, LocaleContextHolder.getLocale());
			return errMessage;
		} catch (final RuntimeException ex) {
			LOGGER.warn("Unable to register user", ex);
			return "Unable to register user";
		}
		return "registered Successfully";
	}

	@GetMapping("/user/resendRegistrationToken")
	public String resendRegistrationToken(final HttpServletRequest request, final Model model,
			@RequestParam("token") final String existingToken) {
		final Locale locale = request.getLocale();
		final VerificationToken newToken = iUserService.generateNewVerificationToken(existingToken);
		final User user = iUserService.getUser(newToken.getToken());
		try {
			final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();
			final SimpleMailMessage email = constructResetVerificationTokenEmail(appUrl, request.getLocale(), newToken,
					user);
			mailSender.send(email);
		} catch (final MailAuthenticationException e) {
			LOGGER.debug("MailAuthenticationException", e);
			return "redirect:/emailError.html?lang=" + locale.getLanguage();
		} catch (final Exception e) {
			LOGGER.debug(e.getLocalizedMessage(), e);
			model.addAttribute("message", e.getLocalizedMessage());
			return "redirect:/login.html?lang=" + locale.getLanguage();
		}
		model.addAttribute("message", messages.getMessage("message.resendToken", null, locale));
		return "redirect:/login.html?lang=" + locale.getLanguage();
	}

	@GetMapping(BaseUrls.RESET_PASSWORD)
	public ResponseEntity<APIResponse<String>> resetPassword(@RequestParam("email") final String userEmail) {
		final User user = iUserService.findUserByEmail(userEmail);
		if (user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(APIResponse.<String>builder()
					.status(Constants.FAILED.getValue()).response(userEmail).message(Messages.EMAIL_NOT_EXIST).build());
		}

		final String token = UUID.randomUUID().toString();
		iUserService.createPasswordResetTokenForUser(user, token);
		try {
			String subject = "Reset Password";
			final String url = appUrl + "/userResetPassword?id=" + user.getId() + "&token=" + token;
			String message = "Please complete the password reset process by clicking on the link below valid for 5 minutes:\n" + url;
			emailService.sendSimpleMessage(user.getUsername(), subject, message);
		} catch (final MailAuthenticationException e) {
			LOGGER.debug("MailAuthenticationException", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(APIResponse.<String>builder().status(Constants.ERROR.getValue()).response(e.getMessage())
							.message(Messages.NETWORK_ERROR).build());
		} catch (final Exception e) {
			LOGGER.debug("Exception occurred while sending email", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(APIResponse.<String>builder().status(Constants.ERROR.getValue()).response(e.getMessage())
							.message(Messages.EMAIL_SEND_ERROR).build());
		}

		return ResponseEntity.ok(APIResponse.<String>builder().status(Constants.SUCCESS.getValue()).response(userEmail)
				.message(Messages.MAIL_SENT_SUCCESSFULLY).build());
	}

	@GetMapping("/validateToken")
	public ResponseEntity<JwtResponse> validateToken(@RequestParam("id") final long id,
			@RequestParam("token") final String token) {
		JwtResponse response = null;
		final PasswordResetToken passToken = iUserService.getPasswordResetToken(token);

		if (passToken == null || passToken.getUser().getId() != id) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
					JwtResponse.builder().accessToken("Password Reset Token not Valid").tokenType(BEARER).build());
		}

		final Calendar cal = Calendar.getInstance();
		if ((passToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
			return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT)
					.body(JwtResponse.builder().accessToken("Password Reset Token Expired").tokenType(BEARER).build());
		}

		try {
			UserDetails userDetails = userDetailService.loadUserByUsername(passToken.getUser().getUsername());
			if (userDetails != null) {
			    // Create the authentication object
			    final Authentication auth = new UsernamePasswordAuthenticationToken(
			        userDetails, null, userDetails.getAuthorities());
			    
			    // Set the authentication in the SecurityContext
			    SecurityContextHolder.getContext().setAuthentication(auth);
			    
			    // Generate the JWT token with claims
			    response = jwtUtils.generateJwtToken(auth, "password_reset"); // Ensure this method is correctly implemented
			    
			    log.info("User is authenticated and JWT token generated for password reset.");
			} else {
			    log.warn("User not found for username: " + passToken.getUser().getUsername());
			}
		} catch (Exception e) {
			throw new UserNotAutharizedException("User not Authorized");

		}

		return ResponseEntity.ok(response);
	}
	
	

	private SimpleMailMessage constructResetVerificationTokenEmail(final String contextPath, final Locale locale,
			final VerificationToken newToken, final User user) {
		final String confirmationUrl = contextPath + "/old/registrationConfirm.html?token=" + newToken.getToken();
		final String message = messages.getMessage("message.resendToken", null, locale);
		final SimpleMailMessage email = new SimpleMailMessage();
		email.setSubject("Resend Registration Token");
		email.setText(message + " \r\n" + confirmationUrl);
		email.setTo(user.getUsername());
		email.setFrom(env.getProperty("support.email"));
		return email;
	}
}
