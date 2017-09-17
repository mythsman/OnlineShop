package com.darglk.onlineshop.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.darglk.onlineshop.security.Authority;
import com.darglk.onlineshop.security.UserRole;


@Entity
@Access(AccessType.FIELD)
public class User implements UserDetails{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;
	
    @Column(name="username", unique=true)
    @NotEmpty(message="username cannot be empty.")
    private String username;
    
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,60}", message="Password is invalid. It should contain at least one: digit, "
			+ "upper, lower case letter, special character and its length should be in range from 6 to 60 chars")
    @Column(name="password")
    private String password;
    
    @Transient
    @Pattern(regexp="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{6,60}", message="Password confirmation is invalid. It should contain at least one: digit, "
			+ "upper, lower case letter, special character and its length should be in range from 6 to 60 chars")
    private String passwordConfirmation;
    
    @Column(name="firstname")
    @NotEmpty(message="firstname cannot be empty.")
    private String firstName;
    
    @Column(name="lastname")
    @NotEmpty(message="lastname cannot be empty.")
    private String lastName;

    @Column(name = "email", nullable = false, unique = true)
    @NotEmpty(message="email address cannot be empty.")
    private String email;
    
    @Column(name = "phone", nullable = false, unique = true)
    @NotEmpty(message="phone number cannot be empty.")
	@Pattern(regexp="(^$|[0-9]{9})", message="Phone number format is not correct (NNNNNNNNN eg.: 700700700).")
    private String phone;

    @Column(name="address")
	@NotEmpty(message="address cannot be empty.")
	private String address;
	
	@Column(name="city")
	@NotEmpty(message="city cannot be empty.")
	private String city;
	
	@NotEmpty(message="post code cannot be empty.")
	@Column(name="postcode")
	@Pattern(regexp="[0-9]{2}\\-[0-9]{3}", message="Post code is incorrect (XX-XXX eg. 20-199).") //Polish zipcode
	private String postcode;
    
    private boolean enabled=true;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy ="user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();
    
    public Set<Order> getOrders() {
		return orders;
	}

	public void setOrders(Set<Order> orders) {
		this.orders = orders;
	}

	public Set<UserRole> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(Set<UserRole> userRoles) {
        this.userRoles = userRoles;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    

    public String getPasswordConfirmation() {
		return passwordConfirmation;
	}

	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
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

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        userRoles.forEach(ur -> authorities.add(new Authority(ur.getRole().getName())));
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username + ", password=" + password
				+ ", passwordConfirmation=" + passwordConfirmation + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + ", phone=" + phone + ", address=" + address + ", city=" + city
				+ ", postcode=" + postcode + ", enabled=" + enabled + ", userRoles=" + userRoles + "]";
	}
    
    
}
