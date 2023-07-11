package com.terzo.portal.repository;

import com.terzo.portal.entity.RefreshToken;
import com.terzo.portal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshTokenRepo extends JpaRepository<RefreshToken,Integer> {
    RefreshToken findByToken(String token);

    int deleteByUser(User byId);

    RefreshToken findByUser(User user);
}
