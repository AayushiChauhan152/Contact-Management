package com.scm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.helper.Helper;
import com.scm.helper.Message;
import com.scm.helper.MessageType;
import com.scm.repository.ContactRepository;
import com.scm.repository.UserRepository;
import com.scm.service.ContactService;
import com.scm.service.ImageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class ContactController {

	@Autowired
	private ContactService contactService;

	@Autowired
	private ImageService imageService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ContactRepository contactRepository;

	private Logger logger = LoggerFactory.getLogger(ContactController.class);

	@GetMapping("/dashboard")
	public String dashboard() {
		return "user/dashboard";
	}

	@GetMapping("/profile")
	public String user_profile(Model model) {
		return "user/profile";
	}

	@GetMapping("/add-contacts")
	public String addContacts(Model model) {
		model.addAttribute("contact", new Contact());
		return "user/addContacts";
	}

	@PostMapping("/do-addContacts")
	public String saveContacts(@Valid @ModelAttribute Contact contact, BindingResult res, Authentication authentication,
			Model m, HttpSession session, @RequestParam("profile_img") MultipartFile file) {

		if (res.hasErrors()) {
			res.getAllErrors().forEach(error -> logger.info(error.toString()));
			Message message = Message.builder().content("There are items that require your attention!!")
					.type(MessageType.red).build();
			session.setAttribute("message", message);
			m.addAttribute("contact", contact);
			return "user/addContacts";
		}

		if (!file.isEmpty()) {
			logger.info(file.getOriginalFilename());

			String fileURL = imageService.uploadImage(file);
			logger.info(fileURL);

			contact.setProfileImg(fileURL);
		} else {
			contact.setProfileImg("contact.png");
		}
		contactService.save(contact, authentication);
		Message message = Message.builder().content("Contact has added Successfully!!").type(MessageType.green).build();
		session.setAttribute("message", message);
		return "redirect:/user/add-contacts";
	}

	@GetMapping("/contacts/{page}")
	public String viewContacts(@PathVariable Integer page, Model model, @RequestParam(required = false) String keyword,
			Authentication authentication) {
		String username = Helper.getEmailOfLoggedInUser(authentication);
		User user = userRepository.findByEmail(username);

		Page<Contact> contacts;

//		(cur page ,contact per page )
		Pageable pageable = PageRequest.of(page, 5);

		if (keyword != null && !keyword.isEmpty()) {
			// If a keyword is provided, search by keyword (name, email, or phone)
			contacts = contactRepository.searchContactsByUser(user.getId(), keyword, pageable);
			model.addAttribute("keyword", keyword);
		} else {
			// If no keyword, return all contacts
			contacts = contactRepository.findContactByUser(user.getId(), pageable);
		}

		model.addAttribute("contacts", contacts);
		model.addAttribute("cur_page", page);
		model.addAttribute("totol_page", contacts.getTotalPages());
		model.addAttribute("total_elements", contacts.getTotalElements());
		return "user/viewContacts";
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable int cid, HttpSession session) {
		Contact contact = this.contactRepository.findById(cid).get();
		contact.setUser(null);

		this.contactRepository.delete(contact);

		logger.info("contact deleted");
		Message message = Message.builder().content("Contact has deleted Successfully!!").type(MessageType.green)
				.build();
		session.setAttribute("message", message);
		return "redirect:/user/contacts/0";
	}

	@GetMapping("/update/{cid}")
	public String updateContact(@PathVariable int cid, Model model) {
		Contact contact = this.contactRepository.findById(cid).get();
		model.addAttribute("contact", contact);
		return "user/updateContact";
	}

	@PostMapping("/process-update")
	public String processUpdate(@Valid @ModelAttribute Contact contact, BindingResult res, Model m,HttpSession session,
			@RequestParam("profile_img") MultipartFile file,Authentication authentication) {
		
		if (res.hasErrors()) {
			res.getAllErrors().forEach(error -> logger.info(error.toString()));
			Message message = Message.builder().content("There are items that require your attention!!")
					.type(MessageType.red).build();
			session.setAttribute("message", message);
			m.addAttribute("contact", contact);
			return "user/updateContact";
		}
		if (contact.getProfile_img()!=null) {
			logger.info(file.getOriginalFilename());

			String fileURL = imageService.uploadImage(file);
			logger.info(fileURL);

			contact.setProfileImg(fileURL);
		}
		this.contactService.update(contact,authentication);		
		Message message = Message.builder().content("Contact has updated Successfully!!")
				.type(MessageType.green).build();
		session.setAttribute("message", message);
		return "redirect:/user/contacts/0";
	}

	@GetMapping("/setting")
	public String userSetting() {
		return "user/setting";
	}
	
	@PostMapping("/doSetting")
	public String processSetting(@RequestParam String oldpswd , @RequestParam String newpswd,HttpSession http,Authentication authentication) {
		
		System.out.println(oldpswd);
		System.out.println(newpswd);
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		User user = userRepository.findByEmail(username);
		
		if(this.bCryptPasswordEncoder.matches(oldpswd, user.getPassword())) {
			
			user.setPassword(this.bCryptPasswordEncoder.encode(newpswd));
			this.userRepository.save(user);
			
			Message message = Message.builder().content("Your password has been changed successfully!!").type(MessageType.green).build();

			http.setAttribute("message", message);
			return "redirect:/user/setting";
		}else {
			Message message = Message.builder().content("Incorrect password!!").type(MessageType.red).build();

			http.setAttribute("message", message);
			return "user/setting";
		}
	}
}
