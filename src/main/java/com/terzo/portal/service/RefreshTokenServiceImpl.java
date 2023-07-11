package com.terzo.portal.service;

import com.terzo.portal.entity.RefreshToken;
import com.terzo.portal.repository.RefreshTokenRepo;
import com.terzo.portal.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService{

    RefreshTokenRepo refreshTokenRepo;
    UserRepo userRepo;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepo refreshTokenRepo, UserRepo userRepo) {
        this.refreshTokenRepo = refreshTokenRepo;
        this.userRepo = userRepo;
    }

    @Override
    public RefreshToken findByToken(String token) {
        return refreshTokenRepo.findByToken(token);
    }

    @Override
    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();
        if(refreshTokenRepo.findByUser(userRepo.findById(userId))==null) {
            refreshToken.setUser(userRepo.findById(userId));
            long refreshTokenDurationMs = 922332036L;
            refreshToken.setExpiryDate(Date.from(Instant.now().plusMillis(refreshTokenDurationMs)));
            refreshToken.setToken(UUID.randomUUID().toString());

            refreshToken = refreshTokenRepo.save(refreshToken);
            return refreshToken;
        }
        else{
            return refreshTokenRepo.findByUser(userRepo.findById(userId));
        }
    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Date.from(Instant.now())) < 0) {
            refreshTokenRepo.delete(token);
            return null;
        }

        return token;
    }
}
