package com.cedrus.attendance.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.cedrus.attendance.entity.HolidayDetail;
import com.cedrus.attendance.repository.HolidayRepository;

@Service("holidayservice")
public class HolidayService {
	
	
	HolidayRepository holidayrepository;
	
	@Autowired
	 public HolidayService(HolidayRepository holidayrepository) {
		this.holidayrepository = holidayrepository;
	}

	public String addUser(HolidayDetail holidaydetail) {
		HolidayDetail holidaydet = (HolidayDetail)holidayrepository.findByHolidayDate(holidaydetail.getHolidayDate());
		if(holidaydet==null) {
		 System.out.println("HELLO"+holidaydetail.getHolidayDate()+"    "+holidaydetail.getReason());
	        holidayrepository.save(holidaydetail);
	        return "Holiday has been added on : " + holidaydetail.getHolidayDate();
	    }
		return "DATA ALREADY THERE";
	}
	 
	    public List<HolidayDetail> findAllHoliday() {
            return (List<HolidayDetail>) holidayrepository.findAll();
        }
	 	

	    public HolidayDetail findByDate(LocalDate date) {
	    	HolidayDetail holidaylist = (HolidayDetail) holidayrepository.findByHolidayDate(date);

	        if (holidaylist != null) {
	            return holidaylist;
	        }
	        return null;
	    }
	 	

	    public String deleteBydeptid(LocalDate date) {
	    	holidayrepository.deleteByHolidayDate(date);
	        return "Deleted Successfully";
	    }
	 
}
