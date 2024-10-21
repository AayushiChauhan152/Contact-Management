package com.scm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.scm.entity.Contact;
import com.scm.entity.User;
import com.scm.helper.Helper;
import com.scm.helper.ResourceNotFoundException;
import com.scm.repository.ContactRepository;
import com.scm.repository.UserRepository;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private UserRepository userRepository;

	public Contact save(Contact contact,Authentication authentication) {
		String username = Helper.getEmailOfLoggedInUser(authentication);
		User user = userRepository.findByEmail(username);
		contact.setUser(user);
		return this.contactRepository.save(contact);
	}
	
	public Contact getById(int cid) {
		return this.contactRepository.findById(cid).get();
	}

	public Contact update(Contact contact,Authentication authentication) {
		Contact contact2 = this.contactRepository.findById(contact.getCid())
				.orElseThrow(() -> new ResourceNotFoundException("Contact not found!!"));
		contact2.setCid(contact.getCid());
		contact2.setCname(contact.getCname());
		contact2.setEmail(contact.getEmail());
		contact2.setAddress(contact.getAddress());
		contact2.setDetail(contact.getDetail());
		contact2.setPhone(contact.getPhone());
		contact2.setWork(contact.getWork());
		contact2.setFav(contact.isFav());
		
		String username = Helper.getEmailOfLoggedInUser(authentication);
		User user = userRepository.findByEmail(username);
		contact2.setUser(user);
		
		Contact save = this.contactRepository.save(contact2);
		return save;
	}

	public List<Contact> getContactsByUser(int userId) {
		List<Contact> list = contactRepository.findByUserId(userId);
		return list;
	}
	
//	public List<Contact> searchContacts(String keyword) {
//        return contactRepository.findByCnameContainingOrEmailContainingOrPhoneContaining(keyword, keyword, keyword);
//    }
}
