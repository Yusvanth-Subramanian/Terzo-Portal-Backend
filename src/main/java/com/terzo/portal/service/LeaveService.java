package com.terzo.portal.service;

import com.terzo.portal.dto.ApplyLeaveDTO;
import com.terzo.portal.dto.GetUserUnapprovedLeavesDTO;
import com.terzo.portal.dto.LeavesYetToBeApprovedDTO;
import com.terzo.portal.dto.UpcomingTimeOffDTO;
import com.terzo.portal.exceptions.IllegalDateInputException;
import com.terzo.portal.exceptions.LeaveTypeNotAvailableException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface LeaveService {

    List<LeavesYetToBeApprovedDTO> getAllPendingLeaveRequests();

    void applyLeave(ApplyLeaveDTO applyLeaveDTO, HttpServletRequest request) throws LeaveTypeNotAvailableException, IllegalDateInputException;

    void approveLeave(int id,HttpServletRequest request);

    List<UpcomingTimeOffDTO> getUsersUpcomingTimeOff(HttpServletRequest request);

    List<GetUserUnapprovedLeavesDTO> getUserLeaves(HttpServletRequest request);

    void updateLeave(GetUserUnapprovedLeavesDTO getUserUnapprovedLeavesDTO) throws LeaveTypeNotAvailableException;

    void deleteLeave(int id);
}
