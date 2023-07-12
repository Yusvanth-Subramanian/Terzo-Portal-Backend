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
public class ListUserDetailsDTO {

    private int id;

    private String name;

    private String designation;

    private String department;

    private Date joiningDate;

    private String email;

    private long mobileNumber;

    private String address;

    private Date dateOfBirth;
}
