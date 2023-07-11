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
public class RegisterDTO {

    private String name;

    private String email;

    private String passwordForUser;

    private String designation;

    private int departmentId;

    private long mobileNumber;

    private String address;

    private String employeeType;

    private int reportsTo;

    private Date dateOfBirth;

    private int roleId;

    private int teamId;
}
