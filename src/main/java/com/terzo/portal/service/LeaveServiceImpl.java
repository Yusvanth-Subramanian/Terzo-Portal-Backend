package com.terzo.portal.service;

import com.terzo.portal.dto.*;
import com.terzo.portal.entity.AppliedLeave;
import com.terzo.portal.entity.User;
import com.terzo.portal.exceptions.IllegalDateInputException;
import com.terzo.portal.exceptions.LeaveTypeNotAvailableException;
import com.terzo.portal.repository.LeavesRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService{

    LeavesRepo leavesRepo;

    EmailService emailService;

    UserService userService;

    @Autowired
    public LeaveServiceImpl(LeavesRepo leavesRepo, UserService userService,EmailService emailService) {
        this.leavesRepo = leavesRepo;
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public List<LeavesYetToBeApprovedDTO> getAllPendingLeaveRequests() {
        return leavesRepo.findByApproved(false)
                .stream()
                .map(this::mapToLeavesYetToBeApproved)
                .toList();
    }

    @Override
    public void applyLeave(ApplyLeaveDTO applyLeaveDTO, HttpServletRequest request) throws LeaveTypeNotAvailableException, IllegalDateInputException {
        AppliedLeave leave = new AppliedLeave();
        if(applyLeaveDTO.getToDate().before(applyLeaveDTO.getFromDate())
                ||applyLeaveDTO.getNote()==null||applyLeaveDTO.getTypeOfLeave()==null){
            throw new IllegalDateInputException();
        }
        User user = userService.getUserFromJwt(request.getHeader("Authorization").substring(7));
        LocalDate localDate1 = applyLeaveDTO.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = applyLeaveDTO.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int days = (int) ChronoUnit.DAYS.between(localDate2, localDate1)*-1;
        if(("earned-leave".equals(applyLeaveDTO.getTypeOfLeave())&&user.getEarnedLeaveLeft()-days<=0)||
                ("sick-leave".equals(applyLeaveDTO.getTypeOfLeave())&&user.getSickLeaveLeft()-days<=0)||
                ("paternity-leave".equals(applyLeaveDTO.getTypeOfLeave())&&user.getPaternityLeaveLeft()-days<=0)
        ){
            throw new LeaveTypeNotAvailableException();
        }
        BeanUtils.copyProperties(applyLeaveDTO,leave);
        leave.setUser(userService.getUserFromJwt(request.getHeader("Authorization").substring(7)));
        leave.setType(applyLeaveDTO.getTypeOfLeave());
        leavesRepo.save(leave);
    }

    @Override
    public void approveLeave(int id,HttpServletRequest request) {
        AppliedLeave leave = leavesRepo.findById(id);
        leave.setApproved(true);
        System.out.println(leave.getUser().getEmail());
        User user = leave.getUser();
        LocalDate localDate1 = leave.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = leave.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int days = (int) ChronoUnit.DAYS.between(localDate1, localDate2)*-1;
        if("earned-leave".equals(leave.getType())){
            user.setEarnedLeaveLeft(user.getEarnedLeaveLeft()-days);
        }
        else if("sick-leave".equals(leave.getType())){
            user.setSickLeaveLeft(user.getSickLeaveLeft()-days);
        }
        else if("paternity-leave".equals(leave.getType())){
            user.setPaternityLeaveLeft(user.getPaternityLeaveLeft()-days);
        }
        String approvedBy = userService.getUserFromJwt(request.getHeader("Authorization").substring(7)).getName();

        emailService.send(
                EmailDTO.builder()
                        .to(user.getEmail())
                        .subject("Update on your leave request")
                        .body("Your leave request has been approved by "+approvedBy)
                        .build()
        );
        userService.updateChanges(user);
        leavesRepo.save(leave);
    }

    @Override
    public List<UpcomingTimeOffDTO> getUsersUpcomingTimeOff(HttpServletRequest request) {
        User user = userService.getUserFromJwt(request.getHeader("Authorization").substring(7));
        List<AppliedLeave> leaves = user.getAppliedLeaves();
        return leaves.stream()
                .filter(
                        leave->leave.getFromDate().after(new Date())&&leave.isApproved())
                .map(this::maptoUpcomingTimeOffDTO)
                .toList();
    }

    @Override
    public List<GetUserUnapprovedLeavesDTO> getUserLeaves(HttpServletRequest request) {
        User user = userService.getUserFromJwt(request.getHeader("Authorization").substring(7));
        List<AppliedLeave> leaves = user.getAppliedLeaves();
        return leaves.stream().filter(
                        leave->leave.getFromDate().after(new Date())&&!leave.isApproved())
                .map(this::mapToGetUserUnapprovedLeavesDTO)
                .toList();

    }

    @Override
    public void updateLeave(GetUserUnapprovedLeavesDTO getUserUnapprovedLeavesDTO) throws LeaveTypeNotAvailableException {
        AppliedLeave leave = leavesRepo.findById(getUserUnapprovedLeavesDTO.getId());
        LocalDate localDate1 = leave.getFromDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = leave.getToDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int days = (int) ChronoUnit.DAYS.between(localDate2, localDate1)*-1;
        User user = leave.getUser();
        if(("earned-leave".equals(leave.getType())&&user.getEarnedLeaveLeft()-days<=0)||
                ("sick-leave".equals(leave.getType())&&user.getSickLeaveLeft()-days<=0)||
                ("paternity-leave".equals(leave.getType())&&user.getPaternityLeaveLeft()-days<=0)
        ){
            throw new LeaveTypeNotAvailableException();
        }
        leave.setFromDate(getUserUnapprovedLeavesDTO.getFromDate());
        leave.setToDate(getUserUnapprovedLeavesDTO.getToDate());
        leave.setNote(getUserUnapprovedLeavesDTO.getNote());
        leave.setType(getUserUnapprovedLeavesDTO.getTypeOfLeave());
        leavesRepo.save(leave);
    }

    @Override
    public void deleteLeave(int id) {
        leavesRepo.delete(leavesRepo.findById(id));
    }

    private GetUserUnapprovedLeavesDTO mapToGetUserUnapprovedLeavesDTO(AppliedLeave leave) {
        return GetUserUnapprovedLeavesDTO.builder()
                .id(leave.getId())
                .typeOfLeave(leave.getType())
                .toDate(leave.getToDate())
                .note(leave.getNote())
                .fromDate(leave.getFromDate())
                .build();
    }

    private UpcomingTimeOffDTO maptoUpcomingTimeOffDTO(AppliedLeave i) {
        UpcomingTimeOffDTO upcomingTimeOffDTO = new UpcomingTimeOffDTO();
        BeanUtils.copyProperties(i,upcomingTimeOffDTO);
        return upcomingTimeOffDTO;
    }

    private LeavesYetToBeApprovedDTO mapToLeavesYetToBeApproved(AppliedLeave i) {

        return LeavesYetToBeApprovedDTO.builder()
                .id(i.getId())
                .fromDate(i.getFromDate())
                .toDate(i.getToDate())
                .note(i.getNote())
                .userName(i.getUser().getName())
                .build();

    }
}
