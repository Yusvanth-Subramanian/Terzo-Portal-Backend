package com.terzo.portal.service;

import com.terzo.portal.dto.TeamResponseDTO;
import com.terzo.portal.entity.Team;

import java.util.List;

public interface TeamService {

    List<TeamResponseDTO> getAllTeams();

    Team getTeamById(int id);

}
