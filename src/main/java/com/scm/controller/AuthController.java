package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.entity.User;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserService userService;

	@GetMapping("/verify-email")
	public String verifyEmail(@RequestParam String token,HttpSession session) {

		User user = null;
		user = userService.getUserByEmailToken(token);

		if (user != null && user.getEmailToken().equals(token)) {
				user.setEnabled(true);
				user.setEmailVerified(true);
				userService.saveUser(user);
				
				Message message = Message.builder().content("Your email is verified. Now you can proceed!!")
						.type(MessageType.green).build();
				
				session.setAttribute("message", message);
				return "success_page";
		}
		Message message = Message.builder().content("Email is not Verified.Something went wrong!!")
				.type(MessageType.red).build();
		
		session.setAttribute("message", message);
		return "error_page";
	}

}
