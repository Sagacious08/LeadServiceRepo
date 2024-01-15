package com.boarding.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.boarding.entity.Lead;
@Component
public class LeadValidator implements Validator{

	private EmailValidator emailValidator=EmailValidator.getInstance();
	
	@Override
	public boolean supports(Class<?> clazz) {
		return clazz==Lead.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		Lead lead=(Lead)target;
		
		if(lead.isFlag()) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "First Name Should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "Last Name Should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "mobileNumber", "Mobile Number Should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender", "Gender Should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "dob", "DOB Should not be empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "Email Should not be empty");
		lead.setFlag(false);
		}
		
		if(!lead.isFlag()) {
			String nameValidation = "(?i)(^[a-z]+)[a-z .,-]((?! .,-)$){1,25}$";
			if (lead.getFirstName()!=null && !lead.getFirstName().matches(nameValidation)) {
				errors.rejectValue("firstName",
						"The first name is between 1 and 25 characters.\r\n"
								+ "The first name can only start with an a-z (ignore case) character.\r\n"
								+ "After that the first name can contain a-z (ignore case) and [ '-,.].\r\n" + "\r\n"
								+ "The first name can only end with an a-z (ignore case) character");
			}

			if (lead.getLastName()!=null && !lead.getLastName().matches(nameValidation)) {
				errors.rejectValue("lastName",
						"The last name is between 1 and 25 characters.\r\n"
								+ "The last name can only start with an a-z (ignore case) character.\r\n"
								+ "After that the last name can contain a-z (ignore case) and [ '-,.].\r\n" + "\r\n"
								+ "The last name can only end with an a-z (ignore case) character");
			}
			if (lead.getEmail()!=null && !emailValidator.isValid(lead.getEmail())) {
		         errors.rejectValue("email", "Email is invalid please enter valid email.");
		     }
			String phonevalidation = "(0/91)?[6-9][0-9]{9}";
			if (lead.getMobileNumber() !=null && !lead.getMobileNumber().matches(phonevalidation)) {
				errors.rejectValue("mobileNumber",
						"(0/91): number starts with (0/91)  \r\n"
								+ "[6-9]: starting of the number may contain a digit between 6 to 9  \r\n"
								+ "[0-9]: then contains digits 0 to 9  ");
			}
			if(lead.getGender()!=null && !lead.getGender().equals("Male") && !lead.getGender().equals("Female") && !lead.getGender().equals("Others")) {
				errors.rejectValue("gender","Gender Should be Male/Female/Others");
			}
			if(lead.getDob()!=null && !isValid(lead.getDob())) {
				errors.rejectValue("DOB","Not valid format of DOB, valid format is dd/MM/yyyy");
			}
			
		}
	}
	public boolean isValid(LocalDate value) {
	    if (value == null) {
	        return false;
	    }

	    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    try {
	        String formattedDate = value.format(dateFormatter);
	        LocalDate parsedDate = LocalDate.parse(formattedDate, dateFormatter);
	        return formattedDate.equals(parsedDate.format(dateFormatter));
	    } catch (DateTimeParseException e) {
	        e.printStackTrace(); // Not good practice
	        return false;
	    }
	}
}
