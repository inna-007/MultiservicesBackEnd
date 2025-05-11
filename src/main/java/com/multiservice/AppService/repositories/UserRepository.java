package com.multiservice.AppService.repositories;

import com.multiservice.AppService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<User> findByActivationToken(String token);
    Optional<User> findByResetToken(String resetToken);
}
