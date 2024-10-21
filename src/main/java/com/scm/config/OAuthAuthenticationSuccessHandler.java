package com.scm.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.scm.entity.Providers;
import com.scm.entity.User;
import com.scm.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	Logger logger = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		logger.info("OAuthAuthenticationSuccessHandler");

//		save the data in database-
//		DefaultOAuth2User user = (DefaultOAuth2User) authentication;

//		logger.info(user.getName());
//		user.getAttributes().forEach((k,v)->{
//			logger.info("{}=>{}",k,v);
//		});
//		logger.info(user.getAttributes().toString());

//		identify the type of provider-

		var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
		String clientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();
		
		logger.info(clientRegistrationId);

		var oauthUser = (DefaultOAuth2User) authentication.getPrincipal();

		if (clientRegistrationId.equalsIgnoreCase("github")) {

			String email = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
					: oauthUser.getAttribute("login").toString() + "@gmail.com";

			String name = oauthUser.getAttribute("name") != null ? oauthUser.getAttribute("name").toString()
					: oauthUser.getAttribute("login").toString();

			String picture = oauthUser.getAttribute("avatar_url").toString();

//			create and save user in database

			User user = new User();
			user.setEmail(email);
			user.setName(name);
			user.setImgUrl(picture);
			user.setProvider(Providers.GITHUB);
			user.setEmailVerified(true);
			user.setEnabled(true);
			user.setProviderUserId(oauthUser.getAttribute("id").toString());
			user.setRole("ROLE_USER");
			user.setAbout(oauthUser.getAttribute("bio"));
			user.setPassword("123");
			user.setAgreed(true);
			
			if(userRepository.findByEmail(email)==null) {
				userRepository.save(user);
				logger.info("user saved");
			}
			
		}

		response.sendRedirect("/user/dashboard");
	}

}
