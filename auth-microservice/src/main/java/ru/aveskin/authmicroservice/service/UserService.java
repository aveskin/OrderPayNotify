package ru.aveskin.authmicroservice.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.aveskin.authmicroservice.entity.User;

public interface UserService {
    User save(User user);

    User create(User user);

    User getByUsername(String username);

    UserDetailsService userDetailsService();

    User getCurrentUser();

    User getByEmail(String email);
}
