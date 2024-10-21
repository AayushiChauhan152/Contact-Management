package com.scm.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.scm.entity.Contact;
import com.scm.entity.User;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

	List<Contact> findByUser(User user);

	@Query("from Contact as c where c.user.id = :userId")
	public Page<Contact> findContactByUser(@Param("userId") int userId, Pageable pageable);

	@Query("from Contact as c where c.user.id = :userId")
	List<Contact> findByUserId(@Param("userId") int userId);

//	it will return contacts according to search keyword
//	List<Contact> findByCnameContainingOrEmailContainingOrPhoneContaining(String name, String email, String phone);

	@Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND "
			+ "(c.cname LIKE %:keyword% OR c.email LIKE %:keyword% OR c.phone LIKE %:keyword%)")
	Page<Contact> searchContactsByUser(@Param("userId") int userId, @Param("keyword") String keyword,
			Pageable pageable);

}
