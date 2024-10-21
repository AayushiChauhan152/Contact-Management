package com.scm.entity;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;

	@NotBlank(message = "Name can't be null!!")
	private String cname;
	
	private String work;
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message="Email is Invalid!!")
	private String email;
	
	@NotBlank(message = "Phone number is required!!")
	@Pattern(regexp="^[0-9]{10}$",message="Invalid Phone Number!!")
	@Column(unique=true)
	private String phone;
	
	private String profileImg; 

    @Transient
    private MultipartFile profile_img; 

	@Column(length = 1000)
	private String address;

	@Column(length = 5000)
	private String detail;

	@Builder.Default
	private boolean fav = false;

	@ManyToOne(cascade = CascadeType.ALL)
	@JsonIgnore
	private User user;
}
