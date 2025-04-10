package ru.aveskin.authmicroservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.aveskin.authmicroservice.entity.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}