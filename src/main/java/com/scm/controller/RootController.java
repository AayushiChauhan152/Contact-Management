package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.scm.entity.User;
import com.scm.helper.Helper;
import com.scm.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ControllerAdvice
public class RootController {

	@Autowired
	private UserRepository userRepository;

	private Logger logger = LoggerFactory.getLogger(ContactController.class);

	@ModelAttribute
	public void loggedInUser(Authentication authentication, Model model) {
		if(authentication==null)return;
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		logger.info(username);

		User user = userRepository.findByEmail(username);

		if (user == null) {
			model.addAttribute("user", null);
		} else {
			logger.info(user.getName());
			model.addAttribute("user", user);
		}
	}

}
