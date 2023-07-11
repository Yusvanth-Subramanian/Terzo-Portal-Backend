package com.terzo.portal.service;

import com.terzo.portal.dto.ApplyLeaveDTO;
import com.terzo.portal.dto.LeavesYetToBeApprovedDTO;
import com.terzo.portal.dto.UpcomingTimeOffDTO;
import com.terzo.portal.exceptions.LeaveTypeNotAvailableException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface LeaveService {

    List<LeavesYetToBeApprovedDTO> getAllPendingLeaveRequests();

    void applyLeave(ApplyLeaveDTO applyLeaveDTO, HttpServletRequest request) throws LeaveTypeNotAvailableException;

    void approveLeave(int id,HttpServletRequest request);

    List<UpcomingTimeOffDTO> getUsersUpcomingTimeOff(HttpServletRequest request);
}
