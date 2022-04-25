package com.prescription.management.repository;

import com.prescription.management.entities.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("FROM Users u WHERE u.email = ?1 or u.contact = ?1")
    Users findByUsername(String username);

    Users findByEmail(String email);

    Users findByContact(String contact);
}
