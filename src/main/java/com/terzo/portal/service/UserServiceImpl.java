package com.terzo.portal.service;

import java.lang.reflect.Field;
import com.terzo.portal.dto.*;
import com.terzo.portal.entity.AppliedLeave;
import com.terzo.portal.entity.Team;
import com.terzo.portal.entity.User;
import com.terzo.portal.exceptions.*;
import com.terzo.portal.repository.UserRepo;
import com.terzo.portal.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService{

    UserRepo userRepo;

    DepartmentService departmentService;

    RoleService roleService;

    JwtUtils jwtUtils;

    TeamService teamService;

    EmailService emailService;

    PasswordEncoder passwordEncoder;

    AuthenticationManager authenticationManager;

    Map<String,Integer> emailToOtp = new HashMap<>();

    List<String> verifiedUsers = new ArrayList<>();

    @Autowired
    public UserServiceImpl(TeamService teamService,UserRepo userRepo, DepartmentService departmentService,
                           RoleService roleService,JwtUtils jwtUtils,EmailService emailService,
                           PasswordEncoder passwordEncoder
            ,AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.jwtUtils = jwtUtils;
        this.teamService = teamService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void save(RegisterDTO registerDTO) throws IllegalAccessException, AllFieldsRequiredException, UserWithThisEmailAlreadyExistsException {

        if(hasNullAttribute(registerDTO)){
            throw new AllFieldsRequiredException();
        }
        if(userRepo.findByEmail(registerDTO.getEmail())!=null){
            throw new UserWithThisEmailAlreadyExistsException();
        }
        User user = new User();
        BeanUtils.copyProperties(registerDTO,user);
        user.setDepartment(departmentService.getDepartmentById(registerDTO.getDepartmentId()));
        user.setPassword(passwordEncoder.encode(registerDTO.getPasswordForUser()));
        user.setRole(roleService.getRoleById(registerDTO.getRoleId()));
        user.setActive(true);
        user.setEarnedLeaveLeft(10);
        user.setSickLeaveLeft(15);
        user.setPaternityLeaveLeft(15);
        user.setDateOfJoining(new Date());
        user.setProfilePicUrl("");
        user.setTeam(teamService.getTeamById(registerDTO.getTeamId()));
        userRepo.save(user);
        emailService.send(
                EmailDTO.builder()
                        .to(user.getEmail())
                        .subject("Welcome to Terzo Portal")
                        .body("""
                                Your Terzo portal account has been opened you . Activate it using the below link\s

                                http://localhost:4200/activate-account""")
                        .build()
        );
    }

    private boolean hasNullAttribute(RegisterDTO registerDTO) throws IllegalAccessException {

        if(registerDTO.getMobileNumber()==0){
            return true;
        }
        for (Field field : registerDTO.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object obj = field.get(registerDTO);
            if (obj == null) {
                return true;
            }
            if (obj instanceof String && ((String) obj).isEmpty()) {
                return true;
            }
            if (obj instanceof Integer && ((Integer) obj) == 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public User findByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public List<ListUserDetailsDTO> getEmployees(int start,int end,String type,String query) {
        if(type.equals("null")||query.equals("null")){
            Pageable paging = PageRequest.of(start,end);
            Page<User> list = userRepo.findAll(paging);

                if (list.hasContent()) {
                    return list.stream().map(this::mapToListUserDetailsDTO).toList();
                }
        }
        Page<User> users = userRepo.findAll(PageRequest.of(start,end,
                Sort.by(Sort.Direction.valueOf(type),query)));

                if(users.hasContent()){
                    return users.stream()
                            .map(this::mapToListUserDetailsDTO).toList();
                }

        return new ArrayList<>();
    }

    @Override
    public CurrentUserProfileDTO getUserDetail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsDTO userDetailsDto = (UserDetailsDTO) auth.getPrincipal();
        User user =  userRepo.findByEmail(userDetailsDto.getEmail());
        CurrentUserProfileDTO currentUserProfileDTO = new CurrentUserProfileDTO();
        BeanUtils.copyProperties(user,currentUserProfileDTO);
        currentUserProfileDTO.setDepartmentName(user.getDepartment().getName());
        currentUserProfileDTO.setUserRole(user.getRole().getName());
        if(userRepo.findById(user.getReportsTo())==null) {
            currentUserProfileDTO.setReportsToUserName("Reports to no one");
        }
        else{
            currentUserProfileDTO.setReportsToUserName(userRepo.findById(user.getReportsTo()).getName());
        }
        return currentUserProfileDTO;
    }

    @Override
    public void deleteUser(int id,HttpServletRequest request) throws SelfDeletionException {
        if(id==getUserFromJwt(request.getHeader("Authorization").substring(7)).getId()){
            throw new SelfDeletionException();
        }
        userRepo.deleteById(id);
    }

    @Override
    public User getUserFromJwt(String jwt) {
        return userRepo.findByEmail(jwtUtils.extractUserName(jwt));
    }

    @Override
    public List<ListUserDetailsDTO> getNewHires() {

        List<User> users = userRepo.findAll();

        return users
                .stream()
                .filter(i->
                        (TimeUnit.MILLISECONDS.toDays(new Date().getTime()-i.getDateOfJoining().getTime()))<=30)
                .map(this::mapToListUserDetailsDTO).toList();
    }

    @Override
    public List<WorkAnniversaryListDTO> getUsersWhoHaveAnniversaryToday() {
        List<User> users = userRepo.findAll();
        return users.stream().filter(user -> {
            LocalDate joiningDate = convertToLocalDate(user.getDateOfJoining());
            LocalDate today = LocalDate.now();
            return joiningDate.getMonth()==today.getMonth()&&joiningDate.getDayOfMonth()== today.getDayOfMonth();
                })
                .map(this::mapToWorkAnniversaryListDTO).toList();
    }

    @Override
    public void updateChanges(User user) {
        userRepo.save(user);
    }

    @Override
    public List<UserDataInCalendarDTO> getCalendarDetails(HttpServletRequest request) {
        User currentUser = getUserFromJwt(request.getHeader("Authorization").substring(7));
        Team usersTeam = currentUser.getTeam();
        List<User> allUsersInTeam = usersTeam.getUsers();
        List<UserDataInCalendarDTO> res = new ArrayList<>();
        for(User user:allUsersInTeam){
            UserDataInCalendarDTO userDataInCalendarDTO = new UserDataInCalendarDTO();
            userDataInCalendarDTO.setName(user.getName());
            userDataInCalendarDTO.setEmail(user.getEmail());
            List<UsersLeaveDTO> usersLeaveDTOList = new ArrayList<>();
            List<AppliedLeave> appliedLeaves = user.getAppliedLeaves();
            for(AppliedLeave leave : appliedLeaves){
                usersLeaveDTOList.add(
                        UsersLeaveDTO.builder()
                                .endDate(leave.getToDate())
                                .startDate(leave.getFromDate())
                                .build()
                );
            }
            userDataInCalendarDTO.setUsersLeaves(usersLeaveDTOList);
            res.add(userDataInCalendarDTO);
        }
        return res;
    }

    @Override
    public boolean generateOTP(String email) {
        Random random = new Random();
        int OTP = 100000 + random.nextInt(900000);

        emailToOtp.put(email,OTP);

        return emailService.send(
                EmailDTO.builder()
                        .to(email)
                        .subject("One Time Password (OTP) for Password change!")
                        .body("OPT - "+OTP)
                        .build()
        );

    }

    @Override
    public boolean validOtp(OtpCheckDTO otpCheckDTO) {
        if(emailToOtp.get(otpCheckDTO.getEmail()).equals(Integer.valueOf(otpCheckDTO.getOtp()))){
            verifiedUsers.add(otpCheckDTO.getEmail());
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(ChangePasswordDTO changePasswordDTO) throws UserNotVerifiedException, UserNotFoundException, InvalidCredentialsException {
        User user = userRepo.findByEmail(changePasswordDTO.getEmail());
        if(user==null){
            throw new UserNotFoundException();
        }
        if(changePasswordDTO.isForForgotPassword()){
            if(verifiedUsers.contains(changePasswordDTO.getEmail())){
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
                if(!user.isActivated()){
                    user.setActivated(true);
                }
                userRepo.save(user);
            }
            else{
                throw new UserNotVerifiedException();
            }
        }
        else{
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(changePasswordDTO.getEmail(), changePasswordDTO.getOldPassword()));
            }
            catch (Exception e){
                throw new InvalidCredentialsException();
            }
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepo.save(user);
        }
    }

    @Override
    public void update(UpdateUserDTO updateUserDTO) throws AllFieldsRequiredException {
        if(updateUserDTO.getEmployeeType()==null||
           updateUserDTO.getRoleId()==0||
           updateUserDTO.getDepartmentId()==0||
           updateUserDTO.getTeamId()==0||
           updateUserDTO.getReportsTo()==0){
            throw new AllFieldsRequiredException();
        }
        User user = userRepo.findById(updateUserDTO.getId());
        user.setActive(updateUserDTO.getActiveStatus().equals("true"));
        user.setReportsTo(updateUserDTO.getReportsTo());
        user.setName(updateUserDTO.getName());
        user.setDesignation(updateUserDTO.getDesignation());
        user.setMobileNumber(updateUserDTO.getMobileNumber());
        user.setAddress(updateUserDTO.getAddress());
        user.setEmail(updateUserDTO.getEmail());
        user.setRole(roleService.getRoleById(updateUserDTO.getRoleId()));
        user.setDepartment(departmentService.getDepartmentById(updateUserDTO.getDepartmentId()));
        user.setTeam(teamService.getTeamById(updateUserDTO.getTeamId()));
        userRepo.save(user);
    }

    @Override
    public void updateLoggedInUserDetails(UpdateLoggedInUserDTO loggedInUserDTO) {
        User user = userRepo.findById(loggedInUserDTO.getId());
        BeanUtils.copyProperties(loggedInUserDTO,user);
        userRepo.save(user);
    }

    @Override
    public List<BirthDayBuddiesDTO> getBirthdayBuddies() {
        List<User> users = userRepo.findAll();
        LocalDate today = LocalDate.now();
        return users.stream()
                .filter(user -> {
                    LocalDate userBirthDate = convertToLocalDate(user.getDateOfBirth());
                    return userBirthDate.getMonth()==today.getMonth()
                            &&userBirthDate.getDayOfMonth()-today.getDayOfMonth()<=7&&
                            userBirthDate.getDayOfMonth()-today.getDayOfMonth()>=0;
                })
                .map(this::mapToBirthDayBuddiesDtoBuilder)
                .toList();
    }


    public static LocalDate convertToLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime())
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    @Override
    public List<GetManagersResponseDTO> getManagers() {
        List<User> users = userRepo.findAll();
        return users.stream()
                .filter(user -> user.getRole().getName().equals("MANAGER"))
                .map(this::mapUserGetManagerResponseDTO)
                .toList();
    }

    @Override
    public String getTotalUsers() {
        return String.valueOf(userRepo.count());
    }

    private GetManagersResponseDTO mapUserGetManagerResponseDTO(User user) {
        return GetManagersResponseDTO
                .builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }

    private BirthDayBuddiesDTO mapToBirthDayBuddiesDtoBuilder(User user) {
        return BirthDayBuddiesDTO.builder()
                .name(user.getName())
                .date(user.getDateOfBirth())
                .department(user.getDepartment().getName())
                .designation(user.getDesignation())
                .build();
    }

    private WorkAnniversaryListDTO mapToWorkAnniversaryListDTO(User user) {
        LocalDate localDate1 = convertToLocalDate(user.getDateOfJoining());
        LocalDate localDate2 = LocalDate.now();
        int years = (int) ChronoUnit.YEARS.between(localDate1, localDate2);
        return WorkAnniversaryListDTO.builder()
                .year(years)
                .department(user.getDepartment().getName())
                .designation(user.getDesignation())
                .name(user.getName())
                .build();
    }

    private ListUserDetailsDTO mapToListUserDetailsDTO(User user) {
        ListUserDetailsDTO listUserDetailsDTO = new ListUserDetailsDTO();
        BeanUtils.copyProperties(user,listUserDetailsDTO);
        listUserDetailsDTO.setDepartment(user.getDepartment().getName());
        listUserDetailsDTO.setJoiningDate(user.getDateOfJoining());
        return listUserDetailsDTO;
    }
}
