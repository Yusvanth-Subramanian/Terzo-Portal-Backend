package com.terzo.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyLeaveDTO {

    private Date fromDate;

    private Date toDate;

    private String note;

    private String typeOfLeave;

}
