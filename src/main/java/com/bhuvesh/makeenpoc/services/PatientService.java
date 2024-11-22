package com.bhuvesh.makeenpoc.services;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bhuvesh.makeenpoc.pojo.Patients;

@Service
public class PatientService {

	
	private final RestTemplate restTemplate = new RestTemplate();
	
	public void saveToAidbox(Patients patients) {
		String aidBoxUrl= "http://localhost:8080/fhir/Patient";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Patients> request = new HttpEntity<>(patients,headers);
		ResponseEntity<String> response = restTemplate.postForEntity(aidBoxUrl, request, String.class);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("Failed to save Patient to AidBox");
		}
	}
	
	public void forwardToSearchService(Patients patients) {
		String searchServiceUrl = "hhtp://localhost:9200/api/search/patients/upsert";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Patients> request = new HttpEntity<>(patients,headers);
		ResponseEntity<String> response = restTemplate.postForEntity(searchServiceUrl, request, String.class);
		if(!response.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("Failed to forward Patient to AidBox SearchService");
		}
	}
}
