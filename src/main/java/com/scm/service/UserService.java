package com.scm.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.scm.entity.User;
import com.scm.repository.UserRepository;
import com.scm.helper.Helper;
import com.scm.helper.ResourceNotFoundException;

@Service
public class UserService {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public User saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setRole("ROLE_USER");

		logger.info(user.getProvider().toString());
		String emailToken = UUID.randomUUID().toString();
		String emailLink = Helper.getLinkForEmailVerification(emailToken);
		user.setEmailToken(emailToken);
		user.setImgUrl("/images/contact.png");
		User save = this.userRepository.save(user);
		
		emailService.sendEmail(save.getEmail(),"Verify Account: Smart Contact Manager", emailLink);
		return save;
	}

	public User getUserById(int id) {
		Optional<User> byId = this.userRepository.findById(id);
		User user = byId.get();
		return user;
	}

	User updateUser(User user) {
		User user2 = this.userRepository.findById(user.getId())
				.orElseThrow(() -> new ResourceNotFoundException("user not found!!"));
		user2.setName(user.getName());
		user2.setEmail(user.getEmail());
		user2.setPassword(user.getPassword());
		user2.setPhone(user.getPhone());
		user2.setAbout(user.getAbout());

		User save = this.userRepository.save(user2);
		return save;
	}

	public void deleteUser(int id) {
		User user2 = this.userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("user not found!!"));
		this.userRepository.delete(user2);
	}

	public boolean isUserExist(int id) {
		User user = this.userRepository.findById(id).get();
		if (user == null)
			return false;
		return true;
	}

	public boolean isUserExistByEmail(String email) {
		User user = this.userRepository.findByEmail(email);
		if (user == null)
			return false;
		return true;
	}

	public List<User> getAllUsers() {
		List<User> list = this.userRepository.findAll();
		return list;
	}
	
	public User getUserByEmailToken(String token) {
		User user = this.userRepository.findByEmailToken(token);
		return user;
	}
	
	public User getByEmail(String email) {
		return this.userRepository.findByEmail(email);
	}
}
