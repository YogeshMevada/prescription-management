package com.prescription.management.service.impl;

import com.prescription.management.constant.UserType;
import com.prescription.management.entities.Role;
import com.prescription.management.entities.Users;
import com.prescription.management.repository.UserRepository;
import com.prescription.management.service.RoleService;
import com.prescription.management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Autowired
    public UserServiceImpl(final UserRepository userRepository,
                           final RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public Users findUserByName(final String username) {
        final Users users = userRepository.findByUsername(username);
        if (Objects.isNull(users)) {
            log.error("User not found");
            throw new UsernameNotFoundException("User not found");
        }
        return users;
    }

    @Override
    public Users findUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Users findUserByContact(final String contact) {
        return userRepository.findByContact(contact);
    }

    @Override
    public void registerNewUser(final Users users, final UserType userType) {
        log.info("Inside Register new user");
        final Role role = roleService.findRole(userType);
        if (Objects.nonNull(role)) {
            users.getRoles().add(role);
        }
        userRepository.save(users);
        log.info("User created successfully");
    }
}
