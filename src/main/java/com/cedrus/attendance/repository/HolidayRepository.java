package com.cedrus.attendance.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cedrus.attendance.entity.HolidayDetail;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends CrudRepository<HolidayDetail, Long> {

    HolidayDetail findByHolidayDate(LocalDate date);

    @Transactional
    void deleteByHolidayDate(LocalDate date);

}	