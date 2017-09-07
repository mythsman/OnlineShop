package com.darglk.onlineshop.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.darglk.onlineshop.dao.PasswordTokenDao;
import com.darglk.onlineshop.dao.RoleDao;
import com.darglk.onlineshop.dao.UserDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.PasswordResetToken;
import com.darglk.onlineshop.security.UserRole;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
    private RoleDao roleDao;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
	private PasswordTokenDao passwordTokenDao;
	
    @Transactional
	public void save(User user) {
        userDao.save(user);
    }

    @Transactional
	public void updateUserPassword(User user) {
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encryptedPassword);
		userDao.save(user);
	}
	
    @Transactional
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
        
    @Transactional
    public User createUser(User user, Set<UserRole> userRoles) {
        User localUser = userDao.findByUsername(user.getUsername());

        if (localUser != null) {
            LOG.info("User with username {} already exist. Nothing will be done. ", user.getUsername());
        } else {
            String encryptedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptedPassword);

            for (UserRole ur : userRoles) {
                roleDao.save(ur.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            localUser = userDao.save(user);
        }

        return localUser;
    }

    @Transactional
    public boolean checkUsernameExists(String username) {
        if (null != findByUsername(username)) {
            return true;
        }

        return false;
    }
    
    @Transactional
    public boolean checkEmailExists(String email) {
        if (null != findByEmail(email)) {
            return true;
        }

        return false;
    }

    @Transactional
    public User saveUser (User user) {
        return userDao.save(user);
    }
    
    public List<User> findUserList() {
        return (List<User>) userDao.findAll();
    }

    @Transactional
    public void enableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(true);
        userDao.save(user);
    }

    @Transactional
    public void disableUser (String username) {
        User user = findByUsername(username);
        user.setEnabled(false);
        System.out.println(user.isEnabled());
        userDao.save(user);
        System.out.println(username + " is disabled.");
    }

	@Override
	@Transactional
	public boolean checkPhoneNumberExists(String phone) {
		if (null != findByPhone(phone)) {
			return true;
		}
		return false;
	}

	@Transactional
	public User findByPhone(String phone) {
		return userDao.findByPhone(phone);
	}
	
	@Transactional
	public void createPasswordResetTokenForUser(User user, String token) {
	    PasswordResetToken myToken = new PasswordResetToken(token, user);
	    passwordTokenDao.save(myToken);
	}

	@Override	
	public void checkEqualityOfPasswords(User user, List<String> errorMessages) {
		if(!checkPasswordsAreEqual(user.getPassword(), user.getPasswordConfirmation())) {
			errorMessages.add("Passwords are not equal.");
		}
	}

	private boolean checkPasswordsAreEqual(String password, String passwordConfirmation) {
		return password.equals(passwordConfirmation);
	}
}
