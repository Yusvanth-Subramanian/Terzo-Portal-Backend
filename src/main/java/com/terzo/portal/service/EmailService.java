package com.terzo.portal.service;

import com.terzo.portal.dto.EmailDTO;

public interface EmailService {

    boolean send(EmailDTO emailDTO);
}
