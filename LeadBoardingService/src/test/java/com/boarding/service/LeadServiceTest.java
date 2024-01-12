package com.boarding.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

import com.boarding.entity.Lead;
import com.boarding.repository.LeadRespository;
import com.boarding.util.ErrorResponse;
import com.boarding.util.LeadValidator;

public class LeadServiceTest {

    @InjectMocks
    private LeadService leadService;

    @Mock
    private LeadValidator leadValidator;

    @Mock
    private LeadRespository leadRepository;
    
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
        
        BindingResult errors = new MapBindingResult(new HashMap<>(), "lead");

        when(leadRepository.findById(any())).thenReturn(Optional.empty());

        JSONObject result = leadService.createLead(lead, errors);

        assertEquals("success", result.get("status"));
        assertEquals("Created Leads Successfully", result.get("data"));

        verify(leadRepository, times(1)).save(lead);
    }

    @Test
    public void testCreateLead_LeadExists() {
        Lead lead = new Lead();
        
        lead.setLeadId(1);
        lead.setFirstName("Sagar");
        lead.setLastName("Tawate");
        lead.setEmail("sagar@gmail.com");
        lead.setDob(new Date());
        lead.setGender("Male");
        lead.setMobileNumber("9359119778");
        
        BindingResult errors = new MapBindingResult(new HashMap<>(), "lead");

        when(leadRepository.findById(any())).thenReturn(Optional.of(lead));

        JSONObject result = leadService.createLead(lead, errors);

        assertEquals("error", result.get("status"));
        ErrorResponse errorResponse = (ErrorResponse) result.get("errorResponse");
        assertEquals("E10010", errorResponse.getCode());
        assertEquals("Lead Already Exists in the database with the lead id", errorResponse.getMessages()[0]);

        verify(leadRepository, never()).save(any());
    }

    @Test
    public void testGetLeadByMobileNumber_Success() {
        String mobileNumber = "9359119778";
        Lead lead = new Lead();
        
        lead.setLeadId(1);
        lead.setFirstName("Sagar");
        lead.setLastName("Tawate");
        lead.setEmail("sagar@gmail.com");
        lead.setDob(new Date());
        lead.setGender("Male");
        lead.setMobileNumber("9359119778");
        List<Lead> leads = new ArrayList<>();
        leads.add(lead);
        
        when(leadRepository.findByMobileNumber(any())).thenReturn(leads);

        JSONObject result = leadService.getLeadByMobileNumber(mobileNumber);

        assertEquals("success", result.get("status"));
        assertEquals(leads, result.get("data"));

        verify(leadRepository, times(1)).findByMobileNumber(mobileNumber);
    }

    @Test
    public void testGetLeadByMobileNumber_NoLeadFound() {
        String mobileNumber = "7276782169";

        when(leadRepository.findByMobileNumber(any())).thenReturn(Collections.emptyList());

        JSONObject result = leadService.getLeadByMobileNumber(mobileNumber);

        assertEquals("error", result.get("status"));
        ErrorResponse errorResponse = (ErrorResponse) result.get("errorResponse");
        assertEquals("E10011", errorResponse.getCode());
        assertEquals("No Lead found with the Mobile Number ", errorResponse.getMessages()[0]);

        verify(leadRepository, times(1)).findByMobileNumber(mobileNumber);
    }

    @Test
    public void testGetLeadByMobileNumber_InvalidMobileNumber() {
        String mobileNumber = "1234567890";

        JSONObject result = leadService.getLeadByMobileNumber(mobileNumber);

        assertEquals("error", result.get("status"));
        ErrorResponse errorResponse = (ErrorResponse) result.get("errorResponse");
        assertEquals("E10011", errorResponse.getCode());
        assertEquals("Validation Failed Enter Valid Mobile Number ", errorResponse.getMessages()[0]);

        verify(leadRepository, never()).findByMobileNumber(any());
    }
}
