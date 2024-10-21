package com.scm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.scm.entity.User;
import com.scm.forms.Userform;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.properties.domain_name}")
	private String to_;

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Smart-contact-management");
		return "home";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Register- Smart-contact-management");
//		blank object 
		m.addAttribute("userForm", new Userform());
		return "register";
	}

	@PostMapping("/do-register")
	public String register(@Valid @ModelAttribute Userform userForm, BindingResult res, HttpSession http, Model model) {

		if (res.hasErrors()) {
			System.out.println(res);
			Message message = Message.builder().content("Something went wrong!!").type(MessageType.red).build();

			http.setAttribute("message", message);
			model.addAttribute("userForm", userForm);
			return "register";
		}
		User user = new User();
		user.setName(userForm.getName());

		user.setAbout(userForm.getAbout());
		user.setEmail(userForm.getEmail());
		user.setPassword(userForm.getPassword());
		user.setPhone(userForm.getPhone());

		this.userService.saveUser(user);
		Message message = Message.builder().content("Registration Successfully!!").type(MessageType.green).build();

		http.setAttribute("message", message);
		return "redirect:/signup";
	}

	@GetMapping("/signin")
	public String login(Model m) {
		m.addAttribute("title", "Login- Smart-contact-management");
		return "login";
	}

	@GetMapping("/send-feedback")
	public String sendFeedback(HttpSession http) {

		http.setAttribute("message",
				Message.builder().content("Thank you for your feedback!").type(MessageType.green).build());
		System.out.println("done!!");

		return "redirect:/"; 
	}

}
