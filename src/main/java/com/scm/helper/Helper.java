package com.scm.helper;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class Helper {

	public static String getEmailOfLoggedInUser(Authentication authentication) {

		String username = "";
		if (authentication instanceof OAuth2AuthenticationToken) {
//			if sign-in is done with github -

			var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;
			String clientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

			var oauthUser = (OAuth2User) authentication.getPrincipal();

			if (clientRegistrationId.equalsIgnoreCase("github")) {

				username = oauthUser.getAttribute("email") != null ? oauthUser.getAttribute("email").toString()
						: oauthUser.getAttribute("login").toString() + "@gmail.com";
			}
		} else {
//			if sign-in is done with manually-
			username = authentication.getName();
		}

		return username;
	}

	public static String getLinkForEmailVerification(String emailToken) {
		String link="http://localhost:8080/auth/verify-email?token="+emailToken;
		return link;
	}
}
