package com.terzo.portal.repository;

import com.terzo.portal.entity.AppliedLeave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavesRepo extends JpaRepository<AppliedLeave,Integer> {

    List<AppliedLeave> findByApproved(boolean b);

    AppliedLeave findById(int id);

}
