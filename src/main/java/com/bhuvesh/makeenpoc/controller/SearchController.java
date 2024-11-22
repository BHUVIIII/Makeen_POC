package com.bhuvesh.makeenpoc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bhuvesh.makeenpoc.pojo.Patients;
import com.bhuvesh.makeenpoc.services.SearchService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/search")
@Tag(name ="Search Management" , description ="API's for patient indexing and searching")
public class SearchController {
	
	private final SearchService searchService;

	public SearchController(SearchService searchService) {
		this.searchService = searchService;
	}
	
	@PostMapping("/patients/upsert")
	@Operation(summary = "Index Patient ", description =" Indexes a patient in OpenSearch")
	public ResponseEntity<String> upsertPatient(@RequestBody Patients patients){
		try {
			searchService.upsertPatient(patients);
			return ResponseEntity.ok("Patient  indexed successfully ");
		}
		catch(Exception e) {
			return ResponseEntity.status(500).body("Error:" + e.getMessage());
		}
	}
	
	@GetMapping("/patients")
	@Operation(summary = "Search patients" , description = "Search patients in OpenSearch using a partial name")
	public ResponseEntity <List<Patients>> searchPatient(@RequestParam String q){
		try {
			List<Patients> results = searchService.searchPatients(q);
			return ResponseEntity.ok(results);
		}
		catch(Exception e) {
			return ResponseEntity.status(500).body(null);
		}
	}
}
	
