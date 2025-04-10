package ru.aveskin.authmicroservice.service;

import ru.aveskin.authmicroservice.dto.JwtAuthenticationResponse;
import ru.aveskin.authmicroservice.dto.SignInRequest;
import ru.aveskin.authmicroservice.dto.SignUpRequest;

public interface AuthenticationService {
    JwtAuthenticationResponse signUp(SignUpRequest request);

    JwtAuthenticationResponse signIn(SignInRequest request);
}
