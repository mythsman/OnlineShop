package com.darglk.onlineshop.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.darglk.onlineshop.service.UserSecurityService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserSecurityService userSecurityService;
    
    private static final String SALT = "salt";

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http
        .csrf().disable()
        .authorizeRequests()
            .antMatchers(
            		"/error/**/*","/webjars/**","/css/**","/images/**","/js/**","/console/**","/forgotPassword",
            		"/","/login*","/login*", "/logout*", "/signin/**", "/signup/**", "/customLogin",
                    "/user/resetPassword*",
                    "/user/changePassword*", "/resources/**", "/product/**").permitAll()
            .antMatchers("/invalidSession*").anonymous()
            .antMatchers("/user/update", "/user/remove", "/cart/**").hasRole("USER")
            .antMatchers("/user/updatePassword*","/user/savePassword*","/updatePassword*").hasAuthority("CHANGE_PASSWORD_PRIVILEGE")
            .anyRequest().hasAuthority("READ_PRIVILEGE")
            
        .and().formLogin()
        .loginPage("/signin")
        .defaultSuccessUrl("/")
        .failureUrl("/signin?error=true").and()
        .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        .logoutSuccessUrl("/signin").deleteCookies("remember-me")
        .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());
    }
}

