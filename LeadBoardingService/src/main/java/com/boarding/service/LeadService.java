package com.boarding.service;

import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.boarding.entity.Lead;
import com.boarding.repository.LeadRespository;
import com.boarding.util.ErrorResponse;
import com.boarding.util.LeadValidator;

@Service
public class LeadService {
	
	@Autowired
	private LeadValidator leadValidator;
	
	@Autowired
	private LeadRespository leadRespository;
	
	public JSONObject createLead(Lead lead,BindingResult errors) {
		JSONObject jsonObject=new JSONObject();
		try {
		lead.setFlag(true);
		leadValidator.validate(lead, errors);
		if (errors.hasErrors()) {
			String [] arr= {errors.getFieldError().getCode()};
			return getResponse(arr,"E10010",jsonObject);
		}
		if(!leadRespository.existsById(lead.getLeadId())) {
			leadRespository.save(lead);
			jsonObject.put("status", "success");
			jsonObject.put("data", "Created Leads Successfully");
			return jsonObject;
		}else {
			String [] arr= {"Lead Already Exists in the database with the lead id"};
			return getResponse(arr,"E10010",jsonObject);
		}
		}catch(Exception e) {
			e.printStackTrace();//Not good practice
			String [] arr= {"Exception Occured while saving in DB"};
			return getResponse(arr,"E10011",jsonObject);
		}
	}

	public JSONObject getLeadByMobileNumber(String mobileNumber) {
		JSONObject jsonObject=new JSONObject();
		try {
		String phonevalidation = "(0/91)?[6-9][0-9]{9}";
		if(mobileNumber!=null && mobileNumber.matches(phonevalidation)) {
			List<Lead> listOfLeads=leadRespository.findByMobileNumber(mobileNumber);
			if(!listOfLeads.isEmpty()) {
				jsonObject.put("status", "success");
				jsonObject.put("data", listOfLeads);
				return jsonObject;
			}else {
				String [] arr= {"No Lead found with the Mobile Number "};
				return getResponse(arr,"E10011",jsonObject);
			}
			}else {
				String [] arr= {"Validation Failed Enter Valid Mobile Number "};
				return getResponse(arr,"E10011",jsonObject);
		}
		}catch(Exception e) {
			e.printStackTrace();//Not good practice
			String [] arr= {"Exception Occured while Getttng Data "};
			return getResponse(arr,"E10011",jsonObject);
		}
	}
	
	public JSONObject getResponse(String [] arr,String rspCode,JSONObject jsonObject) {
		jsonObject.put("status", "error");
		jsonObject.put("errorResponse", new ErrorResponse(rspCode,arr));
		return jsonObject;
	}

}
