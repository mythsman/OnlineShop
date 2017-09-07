package com.darglk.onlineshop.service;

import java.util.Arrays;
import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.darglk.onlineshop.dao.PasswordTokenDao;
import com.darglk.onlineshop.dao.UserDao;
import com.darglk.onlineshop.model.User;
import com.darglk.onlineshop.security.PasswordResetToken;


@Service
public class UserSecurityService implements UserDetailsService {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    @Autowired
    private UserDao userDao;

    @Autowired
	private PasswordTokenDao passwordResetTokenDao;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (null == user) {
            LOG.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        return user;
    }
    
    @Transactional
    public String validatePasswordResetToken(long id, String token) {
        PasswordResetToken passToken = 
          passwordResetTokenDao.findByToken(token);
        if ((passToken == null) || (passToken.getUser()
            .getUserId() != id)) {
            return "invalidToken";
        }
     
        Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
            .getTime() - cal.getTime()
            .getTime()) <= 0) {
        	passwordResetTokenDao.delete(passToken);
            return "expired";
        }
     
        User user = passToken.getUser();
        Authentication auth = new UsernamePasswordAuthenticationToken(
          user, null, Arrays.asList(
          new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
        SecurityContextHolder.getContext().setAuthentication(auth);
        passwordResetTokenDao.delete(passToken);
        return null;
    }
}
