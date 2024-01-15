package com.boarding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boarding.entity.Lead;

public interface LeadRespository extends JpaRepository<Lead, Integer>{

	List<Lead> findByMobileNumber(String mobileNumber);
	
//	boolean existsByLeadId(Integer leadId);
}
