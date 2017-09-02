package com.darglk.onlineshop.service;

import java.util.List;
import java.util.Set;

import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.UserRole;


public interface UserService {
	User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
    
    void save (User user);
    
    User createUser(User user, Set<UserRole> userRoles);
    
    User saveUser (User user); 
    
    List<User> findUserList();

    void enableUser (String username);

    void disableUser (String username);

	boolean checkPhoneNumberExists(String phone);

	void createPasswordResetTokenForUser(User user, String token);
	
	public void updateUserPassword(User user);
}
