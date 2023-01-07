package com.sideproject1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.json.JSONException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sideproject.BaseIntegrationTest;
import com.sideproject1.SprintBootCrudApplication;
import com.sideproject1.exception.InstructorNotFoundException;
import com.sideproject1.model.Instructor;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SprintBootCrudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = OrderAnnotation.class)
@ActiveProfiles("test")
public class InstructorControllerIntegrationTest extends BaseIntegrationTest{

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	@Order(1)
	public void addinstrcutor() {
		Instructor instructor = new Instructor(10001, "instrcutor1","address1");
		
		HttpEntity<Instructor> entity = new HttpEntity<>(instructor, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/instructors/10001"),
				HttpMethod.POST, entity, String.class);
		
		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		
		//assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
		assertTrue(actual.contains("/instructors"));
	}
	
	@Test
	@Order(2)
	public void updateinstructor() throws JSONException{
		
		Instructor instructor = new Instructor(1, "instrcutor1","address1 updated");
		
		HttpEntity<Instructor> entity = new HttpEntity<>(instructor, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/instructors/1"),
				HttpMethod.PUT, entity, String.class);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		
		String expected = "{\"id\":1,\"instructorname\":\"instrcutor1\",\"address\":\"address1 updated\"}";
		
		JSONAssert.assertEquals(expected, response.getBody(), false);
		
	}
	
	@Test
	@Order(3)
	public void testGetInstructor() throws JSONException, JsonProcessingException{
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				createURLWithPort("/api/instructors/1"),
				HttpMethod.GET, entity, String.class);
		
		String expected = "{\"id\":1,\"instructorname\":\"instrcutor1\",\"address\":\"address1 updated\"}";
		
		JSONAssert.assertEquals(expected, response1.getBody(), false);
	}
	
	@Test
	@Order(4)
	public void testDeleteLesson() {
		Instructor instructor = restTemplate.getForObject(createURLWithPort("/api/instructors/1"), Instructor.class);
		assertNotNull(instructor);
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/instructors/1"),
				HttpMethod.DELETE, entity, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
		
		try {
			instructor = restTemplate.getForObject("/api/instructors/1", Instructor.class);
		}catch(InstructorNotFoundException e){
			assertEquals("Instructor id not found : 1", e.getMessage());
		}
	}
	
	private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
