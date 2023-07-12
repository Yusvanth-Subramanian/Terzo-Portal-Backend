package com.terzo.portal.service;

import com.terzo.portal.entity.RefreshToken;

public interface RefreshTokenService {
    RefreshToken findByToken(String token);

    RefreshToken createRefreshToken(int userId);

    RefreshToken verifyExpiration(RefreshToken token);

    void delete(RefreshToken token);

}
