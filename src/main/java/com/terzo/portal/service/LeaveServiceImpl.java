package com.terzo.portal.service;

import com.terzo.portal.dto.ApplyLeaveDTO;
import com.terzo.portal.dto.EmailDTO;
import com.terzo.portal.dto.LeavesYetToBeApprovedDTO;
import com.terzo.portal.dto.UpcomingTimeOffDTO;
import com.terzo.portal.entity.AppliedLeave;
import com.terzo.portal.entity.User;
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
    public void applyLeave(ApplyLeaveDTO applyLeaveDTO, HttpServletRequest request) throws LeaveTypeNotAvailableException {
        AppliedLeave leave = new AppliedLeave();
        User user = userService.getUserFromJwt(request.getHeader("Authorization").substring(7));
        LocalDate localDate1 = user.getDateOfJoining().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int days = (int) ChronoUnit.DAYS.between(localDate1, localDate2);

        if(("Earned".equals(applyLeaveDTO.getTypeOfLeave())&&user.getEarnedLeaveLeft()-days<0)||
                ("Sick".equals(applyLeaveDTO.getTypeOfLeave())&&user.getSickLeaveLeft()-days<0)||
                ("Paternity".equals(applyLeaveDTO.getTypeOfLeave())&&user.getPaternityLeaveLeft()-days<0)
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
        User user = userService.getUserFromJwt(request.getHeader("Authorization").substring(7));
        LocalDate localDate1 = user.getDateOfJoining().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int days = (int) ChronoUnit.DAYS.between(localDate1, localDate2);
        if("Earned".equals(leave.getType())){
            user.setEarnedLeaveLeft(user.getEarnedLeaveLeft()-days);
        }
        else if("Sick".equals(leave.getType())){
            user.setSickLeaveLeft(user.getSickLeaveLeft()-days);
        }
        else if("Paternity".equals(leave.getType())){
            user.setPaternityLeaveLeft(user.getPaternityLeaveLeft()-days);
        }
        emailService.send(
                EmailDTO.builder()
                        .to(user.getEmail())
                        .subject("Update on your leave request")
                        .body("Your leave request has been approved!")
                        .build()
        );
        userService.updateChanges(user);
        leavesRepo.save(leave);
    }

    @Override
    public List<UpcomingTimeOffDTO> getUsersUpcomingTimeOff(HttpServletRequest request) {
        User user = userService.getUserFromJwt(request.getHeader("Authorization").substring(7));
        List<AppliedLeave> leaves = user.getAppliedLeaves();
        System.out.println(leaves.stream().filter(i->i.getFromDate().after(new Date())));
        return leaves.stream().filter(i->i.getFromDate().after(new Date())).map(this::maptoUpcomingTimeOffDTO).toList();
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
