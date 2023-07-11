package com.terzo.portal.service;

import com.terzo.portal.dto.TeamResponseDTO;
import com.terzo.portal.entity.Team;
import com.terzo.portal.repository.TeamRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService{

    TeamRepo teamRepo;

    public TeamServiceImpl(TeamRepo teamRepo) {
        this.teamRepo = teamRepo;
    }

    @Override
    public List<TeamResponseDTO> getAllTeams() {
        return teamRepo.findAll().stream().map(this::mapToTeamResponseDTO).toList();
    }

    @Override
    public Team getTeamById(int id) {
        return teamRepo.findById(id);
    }

    private TeamResponseDTO mapToTeamResponseDTO(Team i) {

        return TeamResponseDTO
                .builder()
                .id(i.getId())
                .name(i.getName())
                .build();

    }
}
