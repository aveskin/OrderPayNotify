package ru.aveskin.authmicroservice.service.impl;

import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.aveskin.authmicroservice.entity.User;
import ru.aveskin.authmicroservice.exception.EmailExistException;
import ru.aveskin.authmicroservice.exception.EmailNotExistException;
import ru.aveskin.authmicroservice.exception.UserExistException;
import ru.aveskin.authmicroservice.exception.UserNotFoundException;
import ru.aveskin.authmicroservice.repository.UserRepository;
import ru.aveskin.authmicroservice.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UserExistException(user.getUsername());
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistException(user.getEmail());
        }

        return save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

    }

    @Override
    @Transactional(readOnly = true)
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        // Получение имени пользователя из контекста Spring Security
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public User getByEmail(@Email(message = "Email адрес должен быть в формате user@example.com") String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EmailNotExistException(email));
    }
}
