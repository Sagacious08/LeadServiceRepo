package com.boarding.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;

import com.boarding.entity.Lead;
import com.boarding.service.LeadService;
import com.boarding.util.ErrorResponse;

public class LeadControllerTest {

    @InjectMocks
    private LeadController leadController;

    @Mock
    private LeadService leadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateLead_Success() {
        Lead lead = new Lead();
        lead.setLeadId(1);
        lead.setFirstName("Sagar");
        lead.setLastName("Tawate");
        lead.setEmail("sagar@gmail.com");
        lead.setDob(new Date());
        lead.setGender("Male");
        lead.setMobileNumber("9359119778");
        JSONObject json = new JSONObject();
        json.put("status", "success");
        json.put("data", "Created Leads Successfully");

        BindingResult errors = new MapBindingResult(new HashMap<>(), "lead");

        when(leadService.createLead(eq(lead), any())).thenReturn(json);

        ResponseEntity<JSONObject> responseEntity = leadController.createLead(lead, errors);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(json, responseEntity.getBody());

        verify(leadService, times(1)).createLead(eq(lead), any());
    }

    @Test
    public void testCreateLead_ValidationError() {
        Lead lead = new Lead();
        lead.setLeadId(1);
        lead.setFirstName("Sagar");
        lead.setLastName("Tawate");
        lead.setEmail("sagar@gmail.com");
        lead.setDob(new Date());
        lead.setGender("BLOB");
        lead.setMobileNumber("9359119778");
        BindingResult errors = mock(BindingResult.class);

        when(errors.hasErrors()).thenReturn(true);

        FieldError fieldError = new FieldError("lead", "gender", "Gender Should be Male/Female/Others");
        when(errors.getFieldError()).thenReturn(fieldError);

        ResponseEntity<JSONObject> responseEntity = leadController.createLead(lead, errors);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        JSONObject responseBody = responseEntity.getBody();
        Assertions.assertEquals("error", responseBody.get("status"));
        ErrorResponse errorResponse = (ErrorResponse) responseBody.get("errorResponse");
        Assertions.assertEquals("E10011", errorResponse.getCode());
        Assertions.assertEquals("Gender Should be Male/Female/Others", errorResponse.getMessages()[0]);

        verify(leadService, times(0)).createLead(any(), any());
    }



    @Test
    public void testGetLeads_Success() {
        String mobileNumber = "";
        Lead lead = new Lead();
        lead.setLeadId(1);
        lead.setFirstName("Sagar");
        lead.setLastName("Tawate");
        lead.setEmail("sagar@gmail.com");
        lead.setDob(new Date());
        lead.setGender("Male");
        lead.setMobileNumber("9359119778");
        
        JSONObject jsonObject = new JSONObject();
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);
        jsonObject.put("status", "success");
        jsonObject.put("data", leads);

        when(leadService.getLeadByMobileNumber(eq(mobileNumber))).thenReturn(jsonObject);

        ResponseEntity<JSONObject> responseEntity = leadController.getLeads(mobileNumber);

        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(jsonObject, responseEntity.getBody());

        verify(leadService, times(1)).getLeadByMobileNumber(eq(mobileNumber));
    }
}