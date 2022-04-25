package com.prescription.management.service.impl;

import com.prescription.management.constant.Status;
import com.prescription.management.entities.Users;
import com.prescription.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailServiceImpl(final UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        log.info("Inside Load User by Username.");
        final Users user = userService.findUserByName(username);
        return new User(username, user.getPassword(), getAuthorities(user));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(final Users user) {
        return user.getRoles().stream()
                .filter(role -> Status.ACTIVE.equals(role.getStatus()))
                .map(role -> {
                    String name = role.getName();
                    if (!name.startsWith("ROLE_")) {
                        name = "ROLE_".concat(name);
                    }
                    final SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(name.toUpperCase());
                    return simpleGrantedAuthority;
                })
                .collect(Collectors.toList());
    }
}
