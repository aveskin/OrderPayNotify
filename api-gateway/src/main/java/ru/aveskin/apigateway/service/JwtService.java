package ru.aveskin.apigateway.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Claims validateToken(String token);
}
