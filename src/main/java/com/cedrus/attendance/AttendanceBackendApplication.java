package com.cedrus.attendance;

import java.util.Locale;
import java.util.Properties;

import org.hibernate.validator.spi.messageinterpolation.LocaleResolver;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@SpringBootApplication
@EnableScheduling
public class AttendanceBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(AttendanceBackendApplication.class, args);
	}

	public RequestContextListener requestContextListener() {
		return new RequestContextListener();
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.gmail.com");
		mailSender.setPort(587);

		mailSender.setUsername("vs653525@gmail.com");
		mailSender.setPassword("eyki iuhp axnu llgl");

	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true"); 
	    props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); 
	    props.put("mail.debug", "true");
		return mailSender;
	}
}
