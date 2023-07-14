package com.terzo.portal.repository;

import com.terzo.portal.entity.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HolidayRepo extends JpaRepository<Holidays,Integer> {

    List<Holidays> findAllByOrderByDate();
}
