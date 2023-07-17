package com.terzo.portal.dto;

import com.terzo.portal.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CurrentUserProfileDTO {

    private int id;

    private String name;

    private String email;


    private String designation;

    private String departmentName;

    private long mobileNumber;

    private String address;

    private String profilePicUrl;

    private Date dateOfJoining;

    private String employeeType;

    private String reportsToUserName;

    private Date dateOfBirth;

    private double earnedLeaveLeft;

    private double sickLeaveLeft;

    private double paternityLeaveLeft;

    private String userRole;

    private String team;

}
