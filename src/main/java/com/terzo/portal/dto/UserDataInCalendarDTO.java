package com.terzo.portal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDataInCalendarDTO {

    private String name;

    private String email;

    private List<UsersLeaveDTO> usersLeaves;
}
