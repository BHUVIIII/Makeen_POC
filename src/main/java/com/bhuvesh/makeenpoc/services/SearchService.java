package com.bhuvesh.makeenpoc.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;

import com.bhuvesh.makeenpoc.pojo.Patients;

@Service
public class SearchService {

	@SuppressWarnings("deprecation")
	private final RestHighLevelClient openSearchClient;
	
	@SuppressWarnings("deprecation")
	public SearchService(RestHighLevelClient openSearchClient) {
		this.openSearchClient = openSearchClient ;
	}
	
	@SuppressWarnings("deprecation")
	public void upsertPatient(Patients patients) throws Exception {
		IndexRequest request = new IndexRequest("patients")
		.id(patients.getId())
		.source(Map.of(
				"name" , patients.getName(),
				"birthdate" , patients.getBirthdate(),
				"gender" , patients.getGender(),
				"phone" , patients.getPhoneNumber()), XContentType.JSON);
		openSearchClient.index(request, RequestOptions.DEFAULT);
	}
	
	@SuppressWarnings("deprecation")
	public List<Patients> searchPatients(String query) throws Exception{
		SearchRequest searchRequest = new SearchRequest("patients");
		searchRequest.source(new SearchSourceBuilder()
				.query(QueryBuilders.matchPhrasePrefixQuery("name",query)));
		SearchResponse response = openSearchClient.search(searchRequest, RequestOptions.DEFAULT);
		List<Patients> results = new ArrayList<>();
		response.getHits().forEach(hit-> {
			Map<String,Object> source = hit.getSourceAsMap();
			Patients patients = new Patients();
			patients.setId(hit.getId());
			patients.setName((String) source.get("name"));
			patients.setBirthdate((String) source.get("birthdate"));
			patients.setGender((String) source.get("gender"));
			patients.setPhoneNumber((String) source.get("phoneNumber"));
			results.add(patients);
		});
		return results;
	}
}
