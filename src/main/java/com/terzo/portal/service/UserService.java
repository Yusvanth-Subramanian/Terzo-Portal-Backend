package com.terzo.portal.service;

import com.terzo.portal.dto.*;
import com.terzo.portal.entity.User;
import com.terzo.portal.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    void save(RegisterDTO registerDTO) throws IllegalAccessException, AllFieldsRequiredException, UserWithThisEmailAlreadyExistsException;

    User findByEmail(String email);

    List<ListUserDetailsDTO> getEmployees(int start,int end,String type,String query);

    CurrentUserProfileDTO getUserDetail();

    void deleteUser(int id,HttpServletRequest request) throws SelfDeletionException;

    User getUserFromJwt(String jwt);

    List<ListUserDetailsDTO> getNewHires();

    List<WorkAnniversaryListDTO> getUsersWhoHaveAnniversaryToday();

    void updateChanges(User user);

    List<UserDataInCalendarDTO> getCalendarDetails(HttpServletRequest request);

    boolean generateOTP(String email);

    boolean validOtp(OtpCheckDTO otpCheckDTO);

    void changePassword(ChangePasswordDTO changePasswordDTO) throws UserNotVerifiedException, UserNotFoundException, InvalidCredentialsException;

    void update(UpdateUserDTO updateUserDTO) throws AllFieldsRequiredException;

    void updateLoggedInUserDetails(UpdateLoggedInUserDTO loggedInUserDTO);

    List<BirthDayBuddiesDTO> getBirthdayBuddies();

    List<GetManagersResponseDTO> getManagers();

    String getTotalUsers();
}
