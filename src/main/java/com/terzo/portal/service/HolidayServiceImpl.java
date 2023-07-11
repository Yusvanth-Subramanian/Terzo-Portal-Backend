package com.terzo.portal.service;

import com.terzo.portal.dto.AddHolidayDTO;
import com.terzo.portal.entity.Holidays;
import com.terzo.portal.repository.HolidayRepo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HolidayServiceImpl implements HolidayService{

    HolidayRepo holidayRepo;

    @Autowired
    public HolidayServiceImpl(HolidayRepo holidayRepo) {
        this.holidayRepo = holidayRepo;
    }

    @Override
    public List<Holidays> getFutureHolidays() {
        return holidayRepo.findAll()
                .stream()
                .filter(i->i.getDate().after(new Date()))
                .toList();
    }

    @Override
    public void save(AddHolidayDTO addHolidayDTO) {
        Holidays holidays = new Holidays();
        BeanUtils.copyProperties(addHolidayDTO,holidays);
        holidayRepo.save(holidays);
    }
}
