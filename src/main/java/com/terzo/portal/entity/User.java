package com.terzo.portal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    private String email;

    private String password;

    private String designation;

    @ManyToOne
    private Department department;

    private long mobileNumber;

    private String address;

    private String profilePicUrl;

    private Date dateOfJoining;

    private String employeeType;

    private int reportsTo;

    private Date dateOfBirth;

    private boolean Active;

    private double earnedLeaveLeft;

    private double sickLeaveLeft;

    private double paternityLeaveLeft;

    private boolean isActivated;

    @ManyToOne
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppliedLeave> appliedLeaves;

    @ManyToOne
    private Team team;


}
