package com.bhuvesh.makeenpoc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhuvesh.makeenpoc.pojo.Patients;
import com.bhuvesh.makeenpoc.services.PatientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient Management" ,description = "API's for managing Patients")
public class PatientController {
	
	private final PatientService patientService;

	public PatientController(PatientService patientService) {
		this.patientService = patientService;
	}
	
	@PostMapping("/add")
	@Operation(summary ="Upsert Patient", description = "Create or update a patient in aidbox and forward to Search Service.")
	public ResponseEntity<String> upsertPatient(@RequestBody Patients patients){
		try {
			patientService.saveToAidbox(patients);
			patientService.forwardToSearchService(patients);
			return ResponseEntity.ok("Patient successfully upserted");
		}
		catch(Exception e) {
			return ResponseEntity.status(500).body("Error:" + e.getMessage());
		}
		
	}

}
