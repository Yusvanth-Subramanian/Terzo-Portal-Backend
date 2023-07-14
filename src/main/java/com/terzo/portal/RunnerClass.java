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

        Department department = departmentRepo.findById(2);

        Role role = roleRepo.findById(2);

        Team team = teamRepo.findById(2);

//       User user = userRepo.findById(2);
//       user.setActivated(true);
//       userRepo.save(user);
//       user = userRepo.findById(3);
//        user.setActivated(true);
//        userRepo.save(user);
//       user = userRepo.findById(4);
//        user.setActivated(true);
//        userRepo.save(user);
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
