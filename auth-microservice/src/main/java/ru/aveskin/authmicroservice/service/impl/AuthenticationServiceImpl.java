package ru.aveskin.authmicroservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.aveskin.authmicroservice.dto.JwtAuthenticationResponse;
import ru.aveskin.authmicroservice.dto.SignInRequest;
import ru.aveskin.authmicroservice.dto.SignUpRequest;
import ru.aveskin.authmicroservice.entity.User;
import ru.aveskin.authmicroservice.exception.UserNotFoundException;
import ru.aveskin.authmicroservice.service.AuthenticationService;
import ru.aveskin.authmicroservice.service.JwtService;
import ru.aveskin.authmicroservice.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Override
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }

    @Override
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        String username;
        username = request.getUsername();
        if (username == null) {
            User user = userService.getByEmail(request.getEmail());
            username = user.getUsername();
        }

        if (username == null) {
            throw new UserNotFoundException("пользователь");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                username,
                request.getPassword()
        ));

        var user = userService
                .userDetailsService()
                .loadUserByUsername(username);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
