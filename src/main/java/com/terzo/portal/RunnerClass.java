package com.terzo.portal;

import com.terzo.portal.entity.Department;
import com.terzo.portal.entity.Role;
import com.terzo.portal.entity.Team;
import com.terzo.portal.entity.User;
import com.terzo.portal.repository.DepartmentRepo;
import com.terzo.portal.repository.RoleRepo;
import com.terzo.portal.repository.TeamRepo;
import com.terzo.portal.repository.UserRepo;
import com.terzo.portal.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class RunnerClass implements CommandLineRunner {

    UserRepo userRepo;

    PasswordEncoder passwordEncoder;

    DepartmentRepo departmentRepo;

    RoleRepo roleRepo;

    TeamRepo teamRepo;

    JwtUtils jwtUtils;

    @Autowired
    public RunnerClass(UserRepo userRepo, PasswordEncoder passwordEncoder,
                       DepartmentRepo departmentRepo, RoleRepo roleRepo,
                       TeamRepo teamRepo, JwtUtils jwtUtils) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.departmentRepo = departmentRepo;
        this.roleRepo = roleRepo;
        this.teamRepo = teamRepo;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void run(String... args) throws Exception {

        jwtUtils.addExpiredJwt("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ5dXN2YW50aHNAZ21haWwuY29tIiwiZXhwIjoxNjg5MTc3NDM0LCJpYXQiOjE2ODkwOTEwMzR9.3bY1ZdkcNwyHEQZtfvLJ_GNLISDB39v-Aqr0TY4VxSU");

//        Department department = departmentRepo.findById(1);
//
//        Role role = roleRepo.findById(2);
//
//        Team team = teamRepo.findById(2);
//
//        userRepo.save(
//                User.builder()
//                        .name("yusvanth")
//                        .email("yusvanths@gmail.com")
//                        .password(passwordEncoder.encode("123"))
//                        .designation("BE")
//                        .department(department)
//                        .mobileNumber(1234567)
//                        .address("Erode")
//                        .profilePicUrl("dp")
//                        .dateOfJoining(new Date())
//                        .employeeType("FULL TIME")
//                        .reportsTo(2)
//                        .dateOfBirth(new Date(102, 6, 21))
//                        .Active(true)
//                        .earnedLeaveLeft(8)
//                        .sickLeaveLeft(10)
//                        .paternityLeaveLeft(15)
//                        .role(role)
//                        .team(team)
//                        .build()
//        );

    }
}
