package com.mythsman.onlineshop;

import com.mythsman.onlineshop.dao.PasswordTokenDao;
import com.mythsman.onlineshop.dao.RoleDao;
import com.mythsman.onlineshop.dao.UserDao;
import com.mythsman.onlineshop.model.User;
import com.mythsman.onlineshop.security.PasswordResetToken;
import com.mythsman.onlineshop.security.Role;
import com.mythsman.onlineshop.security.UserRole;
import com.mythsman.onlineshop.service.UserService;
import com.mythsman.onlineshop.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class TestUserService {

    @TestConfiguration
    static class TestUserServiceContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @Autowired
    private UserService userService;

    @MockBean
    private UserDao userDao;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private PasswordTokenDao passwordTokenDao;

    private User user;

    private UserRole userRole;

    private Role role;

    @Before
    public void setupUserData() {
        user = new User();
        user.setAddress("Somewhere 12");
        user.setCity("Somecity");
        user.setEmail("john@test.com");
        user.setEnabled(true);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("aaZZa44@");
        user.setPasswordConfirmation("aaZZa44@");
        user.setPhone("700700799");
        user.setPostcode("90-691");
        user.setUsername("johndoe");
        role = new Role();
        role.setName("USER_ROLE");
        userRole = new UserRole(user, role);
        role.getUserRoles().add(userRole);
    }

    @Test
    public void testFindByUserName() {
        when(userDao.findByUsername("johndoe")).thenReturn(user);
        User found = userService.findByUsername("johndoe");
        assertThat(found.getUsername().equals("johndoe"));
        assertTrue(userService.checkUsernameExists(user.getUsername()));
        verify(userDao, times(2)).findByUsername("johndoe");
    }

    @Test
    public void testCheckUserNameExistsReturnsFalse() {
        when(userDao.findByUsername("johndoe")).thenReturn(null);
        assertFalse(userService.checkUsernameExists("johndoe"));
        verify(userDao, times(1)).findByUsername("johndoe");
    }

    @Test
    public void testFindByEmail() {
        when(userDao.findByEmail("john@test.com")).thenReturn(user);
        User found = userService.findByEmail("john@test.com");
        assertThat(found.getEmail().equals("john@test.com"));
        assertTrue(userService.checkEmailExists(user.getEmail()));
        verify(userDao, times(2)).findByEmail("john@test.com");
    }

    @Test
    public void testCheckEmailExistsReturnsFalse() {
        when(userDao.findByEmail("john@test.com")).thenReturn(null);
        assertFalse(userService.checkEmailExists("john@test.com"));
        verify(userDao, times(1)).findByEmail("john@test.com");
    }

    @Test
    public void testCreateUserWithExistingUser() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(user);
        User found = userService.createUser(user, user.getUserRoles());
        assertThat(found.getUsername().equals(user.getUsername()));
        verify(userDao, times(1)).findByUsername(user.getUsername());
    }

    @Test
    public void testCreateUserWithNonExistingUser() {
        when(userDao.findByUsername(user.getUsername())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("h4sh3dp4$$");
        when(roleDao.save(role)).thenReturn(role);
        when(userDao.save(user)).thenReturn(user);

        User saved = userService.createUser(user, role.getUserRoles());

        verify(userDao, times(1)).findByUsername(user.getUsername());
        verify(roleDao, times(1)).save(role);
        verify(userDao, times(1)).save(user);

        assertThat(user.getPassword().equals("h4sh3dp4$$"));
        assertThat(saved.getUsername().equals(user.getUsername()));
        assertThat(saved.getUserRoles().size() == 1);
    }

    @Test
    public void testSaveUser() {
        when(userDao.save(user)).thenReturn(user);
        User saved = userService.saveUser(user);
        verify(userDao, times(1)).save(user);
        assertThat(saved.getUsername().equals(user.getUsername()));
    }

    @Test
    public void testSaveUserVer2() {
        when(userDao.save(user)).thenReturn(user);
        userService.save(user);
        verify(userDao, times(1)).save(user);
    }

    @Test
    public void testFindUserList() {
        List<User> userList = new ArrayList<>();
        when(userDao.findAll()).thenReturn(userList);

        List<User> foundList = userService.findUserList();
        verify(userDao, times(1)).findAll();
        assertThat(foundList == userList);
    }

    @Test
    public void testEnableUser() {
        user.setEnabled(false);
        when(userDao.findByUsername("johndoe")).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        userService.enableUser("johndoe");
        verify(userDao, times(1)).findByUsername("johndoe");
        verify(userDao, times(1)).save(user);
        assertTrue(user.isEnabled());
    }

    @Test
    public void testDisableUser() {
        user.setEnabled(true);
        when(userDao.findByUsername("johndoe")).thenReturn(user);
        when(userDao.save(user)).thenReturn(user);
        userService.disableUser("johndoe");
        verify(userDao, times(1)).findByUsername("johndoe");
        verify(userDao, times(1)).save(user);
        assertFalse(user.isEnabled());
    }

    @Test
    public void testCheckPhoneExistsReturnsTrue() {
        when(userDao.findByPhone("000")).thenReturn(user);
        assertTrue(userService.checkPhoneNumberExists("000"));
        verify(userDao, times(1)).findByPhone("000");
    }

    @Test
    public void testCheckPhoneExistsReturnsFalse() {
        when(userDao.findByPhone("000")).thenReturn(null);
        assertFalse(userService.checkPhoneNumberExists("000"));
        verify(userDao, times(1)).findByPhone("000");
    }

    @Test
    public void testCreatePasswordResetTokenForUser() {
        userService.createPasswordResetTokenForUser(user, "token");
        verify(passwordTokenDao, times(1)).save(Mockito.any(PasswordResetToken.class));
    }

    @Test
    public void testCheckEqualityOfPasswordsWithUnequalPasswords() {
        List<String> errorMsg = new ArrayList<>();
        user.setPasswordConfirmation("adskfjl");
        userService.checkEqualityOfPasswords(user, errorMsg);
        assertThat(errorMsg.size() == 1);
    }

    @Test
    public void testCheckEqualityOfPasswordsWithEqualPasswords() {
        List<String> errorMsg = new ArrayList<>();
        userService.checkEqualityOfPasswords(user, errorMsg);
        assertThat(errorMsg.size() == 0);
    }

    @Test
    public void testUpdateUserPassword() {
        String hashedPass = "hashedPassword";
        user = Mockito.mock(User.class);
        when(user.getPassword()).thenReturn("password");
        when(passwordEncoder.encode("password")).thenReturn(hashedPass);

        userService.updateUserPassword(user);

        verify(passwordEncoder, times(1)).encode("password");
        verify(user, times(1)).setPassword(hashedPass);
        verify(user, times(1)).getPassword();
        verify(userDao, times(1)).save(user);
    }
}
