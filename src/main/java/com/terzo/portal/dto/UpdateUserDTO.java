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
public class UpdateUserDTO {

    private int id;


    private String employeeType;

    private int reportsTo;

    private Date dateOfBirth;

    private String activeStatus;

    private int departmentId;

    private long mobileNumber;

    private String address;

    private String designation;

    private String name;

    private String email;

    private int roleId;

    private int teamId;
}
