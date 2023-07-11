package com.terzo.portal.repository;

import com.terzo.portal.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepo extends JpaRepository<Team,Integer> {

    Team findById(int id);
}
