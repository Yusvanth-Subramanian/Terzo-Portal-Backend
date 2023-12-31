package com.terzo.portal.controller;

import com.terzo.portal.dto.*;
import com.terzo.portal.entity.Holidays;
import com.terzo.portal.entity.User;
import com.terzo.portal.exceptions.*;
import com.terzo.portal.handler.ResponseHandler;
import com.terzo.portal.service.*;
import com.terzo.portal.util.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AppController {

    UserService userService;

    LoginService loginService;

    LeaveService leaveService;

    HolidayService holidayService;

    TeamService teamService;

    DepartmentService departmentService;

    JwtUtils jwtUtils;

    RoleService roleService;

    @Autowired
    public AppController(DepartmentService departmentService,UserService userService,
                         LoginService loginService, LeaveService leaveService,
                         HolidayService holidayService, TeamService teamService,
                         JwtUtils jwtUtils,RoleService roleService) {
        this.userService = userService;
        this.loginService = loginService;
        this.leaveService = leaveService;
        this.holidayService = holidayService;
        this.teamService = teamService;
        this.departmentService = departmentService;
        this.roleService = roleService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {

        User user = userService.findByEmail(loginDTO.getEmail());
        if(user==null){
            return ResponseHandler.generateResponse("User not found , please sign up again!!!",HttpStatus.BAD_REQUEST);
        }

        if(!user.isActive()||!user.isActivated())
            return ResponseHandler.generateResponse("User is not active", HttpStatus.FORBIDDEN);

        AuthenticationResponseDTO responseDto = loginService.authenticate(loginDTO);

        if (responseDto != null)
            return ResponseHandler.generateResponse( responseDto,"Login Successful", HttpStatus.OK);
        else
            return ResponseHandler.generateResponse("Try again",HttpStatus.EXPECTATION_FAILED);

    }

    @GetMapping("/get-employees/{start}/{end}/{sortType}/{columnName}/{search}")
    public ResponseEntity<Object> listAllUser(
            @PathVariable("start")int start,@PathVariable("end")int end,
            @PathVariable("sortType")String sortType,@PathVariable("columnName")String columnName,
            @PathVariable("search")String search
    ){
        List<ListUserDetailsDTO> detailsDTOS = userService.getEmployees(start,end,sortType,columnName,search);
        return ResponseHandler.generateResponse(detailsDTOS,"User details retrieved",HttpStatus.OK);
    }

    @GetMapping("/get-employee/details")
    public ResponseEntity<Object> getUserProfile(){
        CurrentUserProfileDTO currentUserProfileDTO = userService.getUserDetail();
        return ResponseHandler.generateResponse(currentUserProfileDTO,"Logged In user details retrieved",HttpStatus.OK);
    }

    @DeleteMapping("/delete-user")
    public ResponseEntity<Object> deleteUser(@RequestParam("id")int id,HttpServletRequest request) throws SelfDeletionException {
        userService.deleteUser(id,request);
        return ResponseHandler.generateResponse("User deleted",HttpStatus.OK);
    }

    @GetMapping("/get-unapproved-leaves")
    public ResponseEntity<Object> getAppliedLeaves(HttpServletRequest request){
        List<LeavesYetToBeApprovedDTO> leaves = leaveService.getAllPendingLeaveRequests(request);
        return ResponseHandler.generateResponse(leaves,"Leavese that are yet to be approved are retrieved",HttpStatus.OK);
    }

    @PostMapping("/save-user")
    public ResponseEntity<Object> saveUser(@RequestBody RegisterDTO registerDTO) throws IllegalAccessException, AllFieldsRequiredException, UserWithThisEmailAlreadyExistsException {
        userService.save(registerDTO);
        return ResponseHandler.generateResponse("User saved",HttpStatus.OK);
    }

    @PostMapping("/apply-leave")
    public ResponseEntity<Object> applyLeave(@RequestBody ApplyLeaveDTO applyLeaveDTO, HttpServletRequest request) throws LeaveTypeNotAvailableException, AllFieldsRequiredException {
        leaveService.applyLeave(applyLeaveDTO,request);
        return ResponseHandler.generateResponse("Leave Applied successfully !",HttpStatus.OK);
    }

    @GetMapping("/approve-leave")
    public ResponseEntity<Object> approveLeave(@RequestParam("id")int id,HttpServletRequest request){
        leaveService.approveLeave(id,request);
        return ResponseHandler.generateResponse("Leave approved!!!",HttpStatus.OK);
    }

    @GetMapping("/get-holidays")
    public ResponseEntity<Object> getListOfHolidays(){
        List<Holidays> holidays = holidayService.getFutureHolidays();
        return ResponseHandler.generateResponse(holidays,"Holiday details retrieved",HttpStatus.OK);
    }

    @GetMapping("/get-user-timeoffs")
    public ResponseEntity<Object> getUsersUpcomingTimeOff(HttpServletRequest request){
        List<UpcomingTimeOffDTO> upcomingLeaves = leaveService.getUsersUpcomingTimeOff(request);
        return ResponseHandler.generateResponse(upcomingLeaves,"Upcoming time offs retrieved",HttpStatus.OK);
    }


    @GetMapping("/get-new-hires")
    public ResponseEntity<Object> getNewHires(){
        List<ListUserDetailsDTO> details = userService.getNewHires();
        return ResponseHandler.generateResponse(details,"Retrieved new hires",HttpStatus.OK);
    }

    @GetMapping("/get-work-anniversary-list")
    public ResponseEntity<Object> getWorkAnniversaryList(){
        List<WorkAnniversaryListDTO> workAnniversaryListDTOS = userService.getUsersWhoHaveAnniversaryToday();
        return ResponseHandler.generateResponse(workAnniversaryListDTOS,"Work anniversary list retrieved",HttpStatus.OK);
    }

    @GetMapping("/get-all-teams")
    public ResponseEntity<Object> getAllTeams(){
        List<TeamResponseDTO> teams = teamService.getAllTeams();
        return ResponseHandler.generateResponse(teams,"All teams retrieved",HttpStatus.OK);
    }

    @GetMapping("/get-all-departments")
    public ResponseEntity<Object> getAllDepartments(){
        List<DepartmentResponseDTO> departments = departmentService.getAllDepartments();
        return ResponseHandler.generateResponse(departments,"All teams retrieved",HttpStatus.OK);
    }

    @PostMapping("/add-holiday")
    public ResponseEntity<Object> addHoliday(@RequestBody AddHolidayDTO addHolidayDTO){
        holidayService.save(addHolidayDTO);
        return ResponseHandler.generateResponse("Holiday saved",HttpStatus.OK);
    }

    @GetMapping("/get-calendar-data")
    public ResponseEntity<Object> getCalenderDate(HttpServletRequest request){
        List<UserDataInCalendarDTO> userDataInCalendarDTOS = userService.getCalendarDetails(request);
        return ResponseHandler.generateResponse(userDataInCalendarDTOS,"Calendar details retrieved",HttpStatus.OK);
    }

    @GetMapping("/generate-otp")
    public ResponseEntity<Object> generateOtpForgotPassword(@RequestParam("email")String email){

        if(userService.generateOTP(email)){
            return ResponseHandler.generateResponse("OTP generated",HttpStatus.OK);
        }
        else{
            return ResponseHandler.generateResponse("Error in sending mail Try again",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping("/check-otp")
    public ResponseEntity<Object> checkOtp(@RequestBody OtpCheckDTO otpCheckDTO){
        System.out.println(otpCheckDTO);
        if(userService.validOtp(otpCheckDTO)){
            return ResponseHandler.generateResponse("Otp verified",HttpStatus.OK);
        }
        else{
            return ResponseHandler.generateResponse("Incorrect Otp!",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) throws UserNotFoundException, UserNotVerifiedException, InvalidCredentialsException {
        userService.changePassword(changePasswordDTO);
        return ResponseHandler.generateResponse("Password changed successfully",HttpStatus.OK);
    }

    @GetMapping("/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request){
        jwtUtils.logout(request);
        return ResponseHandler.generateResponse("User logged out",HttpStatus.OK);
    }

    @PutMapping("/update-user")
    public ResponseEntity<Object> update(@RequestBody UpdateUserDTO updateUserDTO) throws AllFieldsRequiredException {
        userService.update(updateUserDTO);
        return ResponseHandler.generateResponse("User Updated",HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Object> updateCurrentUser(@RequestBody UpdateLoggedInUserDTO loggedInUserDTO){
        userService.updateLoggedInUserDetails(loggedInUserDTO);
        return ResponseHandler.generateResponse("Changes updated",HttpStatus.OK);
    }

    @GetMapping("/get-birthday-buddies")
    public ResponseEntity<Object> getBirthdayBuddies(){
        return ResponseHandler.generateResponse(
                userService.getBirthdayBuddies(),
                "Birthday Buddies list retrieved",HttpStatus.OK
        );
    }

    @GetMapping("/get-user-unapproved-leaves")
    public ResponseEntity<Object> getUserLeaves(HttpServletRequest request){
        return ResponseHandler.generateResponse(
                leaveService.getUserLeaves(request),
                "User's unapproved leaves retrieved",HttpStatus.OK
        );
    }

    @PutMapping("/update-leave")
    public ResponseEntity<Object> updateLeave(@RequestBody GetUserUnapprovedLeavesDTO getUserUnapprovedLeavesDTO) throws LeaveTypeNotAvailableException {
        leaveService.updateLeave(getUserUnapprovedLeavesDTO);
        return ResponseHandler.generateResponse("Leave updated",HttpStatus.OK);
    }

    @DeleteMapping("/delete-leave/{id}")
    public ResponseEntity<Object> deleteLeave(@PathVariable("id")int id){
        leaveService.deleteLeave(id);
        return ResponseHandler.generateResponse("Leave deleted",HttpStatus.OK);
    }

    @GetMapping("/get-managers")
    public ResponseEntity<Object> getListOfManagers(){
        return ResponseHandler.generateResponse(
                userService.getManagers(),"Managers names retrieved",HttpStatus.OK
        );
    }

    @DeleteMapping("/disapprove-leave")
    public ResponseEntity<Object> disapproveLeave(@RequestParam("id")int id){
        leaveService.disapprove(id);
        return ResponseHandler.generateResponse("Leave disapproved",HttpStatus.OK);
    }

    @GetMapping("/get-total-users")
    public ResponseEntity<Object> getTotalUsers(){
        return ResponseHandler.generateResponse(userService.getTotalUsers(),"Total users retrieved",HttpStatus.OK);
    }

    @GetMapping("/get-all-roles")
    public ResponseEntity<Object> getAllRoles(){
        return ResponseHandler.generateResponse(
                roleService.getRoles(),"Roles retrieved",HttpStatus.OK
        );
    }

}
