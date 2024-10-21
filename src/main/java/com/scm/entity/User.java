package com.scm.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private String name;
	private String password;
	
	private String imgUrl, phone;

	@Column(unique = true)
	private String email;
	
	private String role;
	 @Builder.Default
	private boolean enabled=false;
	 
	 private String emailToken;
	
	@Builder.Default
	private boolean  emailVerified = false;
	@Builder.Default
	private boolean phoneVerified = false;

	@Builder.Default
	private boolean agreed=true;

	@Column(length = 500)
	private String about;

	@Enumerated
	@Builder.Default
	private Providers provider=Providers.SELF;
	
	private String providerUserId;

	@Builder.Default
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private List<Contact> contacts = new ArrayList<>();

}
