package com.boarding.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.boarding.entity.Lead;
import com.boarding.service.LeadService;
import com.boarding.util.ErrorResponse;

@RestController
public class LeadController {
	
	@Autowired
	private LeadService leadService;

	@PostMapping("/leads")
	public ResponseEntity<JSONObject> createLead(@RequestBody Lead lead,BindingResult errors){
		JSONObject jsonObject=new JSONObject();
		if (errors.hasErrors()){
			String [] arr= {errors.getFieldError().getDefaultMessage()};
			jsonObject.put("status", "error");
			jsonObject.put("errorResponse", new ErrorResponse("E10011",arr));
            return new ResponseEntity<JSONObject>(jsonObject,HttpStatus.BAD_REQUEST);
        }
		return new ResponseEntity<JSONObject>(leadService.createLead(lead,errors),HttpStatus.OK);
		
	}
	
	@GetMapping("/getLeads/{mobileNumber}")
	public ResponseEntity<JSONObject> getLeads(@PathVariable("mobileNumber") String mobileNumber){
		
		return new ResponseEntity<JSONObject>(leadService.getLeadByMobileNumber(mobileNumber),HttpStatus.OK);
		
	}
}
