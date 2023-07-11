package com.terzo.portal.service;

import com.terzo.portal.dto.AddHolidayDTO;
import com.terzo.portal.entity.Holidays;

import java.util.List;

public interface HolidayService {

    List<Holidays> getFutureHolidays();

    void save(AddHolidayDTO addHolidayDTO);
}
