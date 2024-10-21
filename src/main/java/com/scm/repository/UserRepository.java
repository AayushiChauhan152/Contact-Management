package com.scm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.scm.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer>{

	public User findByEmail(String email);
	
	public User findByEmailAndPassword(String email,String password);
	
	public User findByEmailToken(String emailToken);
}
