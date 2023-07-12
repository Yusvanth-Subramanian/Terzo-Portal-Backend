package com.terzo.portal.service;

import com.terzo.portal.dto.AuthenticationResponseDTO;
import com.terzo.portal.dto.LoginDTO;
import com.terzo.portal.entity.User;
import com.terzo.portal.repository.UserRepo;
import com.terzo.portal.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    JwtUtils jwtUtils;

    UserDetailsService userDetailsService;
    UserRepo userRepo;

    AuthenticationManager authenticationManager;

    RefreshTokenService refreshTokenService;

    @Autowired
    public LoginService(JwtUtils jwtUtils, UserDetailsService userDetailsService, UserRepo userRepo, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.userRepo = userRepo;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    public AuthenticationResponseDTO authenticate(LoginDTO loginDTO){
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword())
            );
            User user = userRepo.findByEmail(loginDTO.getEmail());
            return AuthenticationResponseDTO
                    .builder()
                    .jwt(jwtUtils.generateJwt(loginDTO.getEmail()))
                    .refreshToken(refreshTokenService.createRefreshToken(user.getId()).getToken())
                    .userRoles(user.getRole().getName())
                    .build();
        }
        catch (Exception e){
            return null;
        }
    }
}
