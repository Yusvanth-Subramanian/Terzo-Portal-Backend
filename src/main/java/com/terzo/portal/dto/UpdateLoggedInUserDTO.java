package com.terzo.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateLoggedInUserDTO {

    private int id;

    private String name;

    private String email;

    private String designation;

    private long mobileNumber;

    private String address;

    private String profilePicUrl;

    private Date dateOfBirth;
}
