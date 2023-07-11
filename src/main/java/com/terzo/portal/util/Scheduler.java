package com.terzo.portal.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class Scheduler {

    JwtUtils jwtUtils;

    @Autowired
    public Scheduler(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Scheduled(cron = "0 0 * * * *")
    public void cleanExpired(){
        jwtUtils.cleanExpired();
    }
}
