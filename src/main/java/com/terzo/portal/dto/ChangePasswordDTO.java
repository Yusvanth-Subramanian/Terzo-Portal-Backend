package com.terzo.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordDTO {

    private String newPassword;

    private boolean forForgotPassword;

    private String email;

    private String oldPassword;

}
