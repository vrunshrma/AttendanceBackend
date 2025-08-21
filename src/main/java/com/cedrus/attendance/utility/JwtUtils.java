package com.cedrus.attendance.utility;

import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.cedrus.attendance.bean.AuthenticateUser;
import com.cedrus.attendance.common.BaseUrls;
import com.cedrus.attendance.common.Constants;
import com.cedrus.attendance.config.ReadJWTConfig;
import com.cedrus.attendance.entity.Role;
import com.cedrus.attendance.entity.RoleType;
import com.cedrus.attendance.entity.User;
import com.cedrus.attendance.entity.UserRole;
import com.cedrus.attendance.repository.RoleRepository;
import com.cedrus.attendance.repository.UserRepository;
import com.cedrus.attendance.request.LoginRequest;
import com.cedrus.attendance.response.JwtResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@AllArgsConstructor
public class JwtUtils {

	private static final String BEARER = "Bearer";

	private static final Long SEVEN_DAYS_LOGIN_DURATION = 604800000L;

	private ReadJWTConfig jwtConfig;

	private ModelMapper modelMapper;

	private RoleRepository roleRepository;

	private UserRepository userRepository;
	
	private static Map<String, String> secretKeys = new ConcurrentHashMap();

	public JwtResponse generateJwtToken(Authentication authentication, LoginRequest request, String tokenType) {
		final User userPrincipal = (User) authentication.getPrincipal();
		Long expireIn = request.isRememberMe() ? SEVEN_DAYS_LOGIN_DURATION : Long.parseLong(jwtConfig.getValidity());
		log.info("Generating Jwt token expire in {}", expireIn);
		if (!ObjectUtils.isEmpty(userPrincipal)) {
			final AuthenticateUser authUser = getUserDetails(userPrincipal);
			return JwtResponse.builder()
					.accessToken(Jwts.builder().setSubject(userPrincipal.getUsername()).claim("token_type", tokenType)
							.claim("allowed_url", BaseUrls.USER_BASE_URL)
							.claim("userId", authUser.getId()).setIssuedAt(new Date())
							.claim("role", authUser.getRole())
							.claim("expiry", expireIn)
							.setExpiration(new Date((new Date()).getTime() + expireIn))
							.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey()).compact())
					.tokenType(BEARER).authenticateUser(authUser).build();
		}
		return null;
	}

	public JwtResponse generateJwtToken(Authentication authentication, String tokenType) {
		final User userPrincipal = (User) authentication.getPrincipal();
		Long expireIn = 360000L; // Token expiration time (6 minutes)
		log.info("Generating Jwt token for password_reset expire in {}", expireIn);

		if (!ObjectUtils.isEmpty(userPrincipal)) {
			final AuthenticateUser authUser = getUserDetails(userPrincipal);
			String accessToken = Jwts.builder().setSubject(userPrincipal.getUsername()).claim("token_type", tokenType)
					.claim("allowed_url", "/api/v1/user/resetPassword")
					.setIssuedAt(new Date())
					.claim("userId", authUser.getId()).setIssuedAt(new Date())
					.claim("role", authUser.getRole())
					.claim("expiry", expireIn)
					.setExpiration(new Date(System.currentTimeMillis() + expireIn))
					.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecretKey()).compact();

			return JwtResponse.builder().accessToken(accessToken).tokenType(BEARER).authenticateUser(authUser).build();
		}
		return null;
	}

	public AuthenticateUser getUserDetails(final User user) {
		Set<String> roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
		AuthenticateUser authenticateUser = modelMapper.map(user, AuthenticateUser.class);
		roles.stream().forEach(authenticateUser::setRole);
		return authenticateUser;
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken, String expectedTokenType, String requestedUrl) {
		try {
			Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecretKey()).parseClaimsJws(authToken).getBody();
			String tokenType = claims.get("token_type", String.class);
			String allowedUrl = claims.get("allowed_url", String.class);
			if(expectedTokenType.equalsIgnoreCase("password_reset")) {
				return tokenType != null && tokenType.equals(expectedTokenType)
						&& allowedUrl.equals(requestedUrl);
			}else if(expectedTokenType.equalsIgnoreCase("general_access")){
				return tokenType != null && tokenType.equals(expectedTokenType)
						&& !requestedUrl.equals("/api/v1/user/resetPassword");
			}else return false;
		
		} catch (SignatureException e) {
			log.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			log.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			log.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			log.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			log.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

}
