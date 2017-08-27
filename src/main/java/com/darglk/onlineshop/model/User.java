package com.darglk.onlineshop.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name="user")
@Access(AccessType.FIELD)
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private Long id;
	
	@Column(name="username", unique=true)
	@NotEmpty(message="username cannot be empty.")
	@Pattern(regexp="^[a-zA-Z0-9_-]{3,15}", message="Invalid user name (only alphanumeric characters, 3-15 length).")
	private String userName;
	
	@NotEmpty(message="first name cannot be empty.")
	@Column(name="firstname")
	private String firstName;
	
	@NotEmpty(message="last name cannot be empty.")
	@Column(name="lastname")
	private String lastName;
	
	@Email
	@NotEmpty(message="email name cannot be empty.")
	@Column(name="email", unique=true)
	private String emailAddress;
	
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,60}", message="Password is invalid. It should contain at least one: digit, "
			+ "upper, lower case letter, special character and its length should be in range from 6 to 60 chars")
	@NotEmpty(message="password cannot be empty.")
	@Column(name="password")
	private String password;
	
	@Transient
	@NotEmpty
	@Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,60}", message="Password confirmation is invalid. It should contain at least one: digit, "
			+ "upper, lower case letter, special character and its length should be in range from 6 to 60 chars")
	private String passwordConfirmation;
	
	
	@Column(name="phone", unique=true)
	@NotEmpty(message="phone number cannot be empty.")
	@Pattern(regexp="(^$|[0-9]{10})")
	private String phoneNumber;
	
	@Column(name="address")
	@NotEmpty(message="address cannot be empty.")
	private String address;
	
	@Column(name="city")
	@NotEmpty(message="city cannot be empty.")
	private String city;
	
	@NotEmpty(message="post code cannot be empty.")
	@Column(name="postcode")
	@Pattern(regexp="[0-9]{2}\\-[0-9]{3}") //Polish zipcode
	private String postcode;
	
	@Column(name="enabled")
	private boolean enabled;

	public User(String userName, String firstName, String secondName, String emailAddress, String password,
			String passwordConfirmation, String phoneNumber, String address, String city, String postcode,
			boolean enabled) {
		super();
		this.userName = userName;
		this.firstName = firstName;
		this.lastName = secondName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.passwordConfirmation = passwordConfirmation;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.city = city;
		this.postcode = postcode;
		this.enabled = enabled;
	}
	
	public User() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String secondName) {
		this.lastName = secondName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
