package com.tamthong.trackr.auth;

import com.tamthong.trackr.auth.dto.*;
import com.tamthong.trackr.core.exception.DuplicateResourceException;
import com.tamthong.trackr.core.exception.ResourceNotFoundException;
import com.tamthong.trackr.core.security.JwtService;
import com.tamthong.trackr.user.User;
import com.tamthong.trackr.user.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username is already taken");
        }
        if (request.email() != null && userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email is already taken");
        }

        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .passwordHash(passwordEncoder.encode(request.password()))
                .role("USER")
                .build();

        user = userRepository.save(user);

        String jwt = jwtService.generateAccessToken(user.getUsername(), Map.of("roles", List.of(user.getRole())));
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());

        return new AuthResponse(jwt, refreshToken.getToken(), userDto);
    }

    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String jwt = jwtService.generateAccessToken(user.getUsername(), Map.of("roles", java.util.List.of(user.getRole())));
        refreshTokenService.deleteByUserId(user.getId());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getCreatedAt(), user.getUpdatedAt());

        return new AuthResponse(jwt, refreshToken.getToken(), userDto);
    }

    public TokenRefreshResponse refreshToken(TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String jwt = jwtService.generateAccessToken(user.getUsername(), Map.of("roles", java.util.List.of(user.getRole())));
                    return new TokenRefreshResponse(jwt, request.refreshToken());
                })
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token is not in database!"));
    }
}
