package com.sideproject1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import com.sideproject1.exception.LessonNotFoundException;
import com.sideproject1.model.Instructor;
import com.sideproject1.model.Lesson;
import com.sideproject1.model.Student;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SprintBootCrudApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(value = OrderAnnotation.class)
@ActiveProfiles("test")
public class LessonControllerIntegrationTest extends BaseIntegrationTest {

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	@Order(1)
	public void addlesson() {
		Lesson lesson = new Lesson(1,"Spring Boot Introduction","SpringBoot");
		Student student = new Student(1,"student1","contact1");
		List<Student> students = new ArrayList<>();
		Instructor instructor = new Instructor(1, "instrcutor1","address1");
		lesson.setInstructor(instructor);
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson);
		students.add(student);
		student.setLessons(lessons.stream().collect(Collectors.toSet()));
		lesson.setStudent(students.stream().collect(Collectors.toSet()));

		
		HttpEntity<Instructor> entity1 = new HttpEntity<>(instructor, getHttpHeader());
		
		 restTemplate.exchange(
				createURLWithPort("/api/instructors/1"),
				HttpMethod.POST, entity1, String.class);
		
		HttpEntity<Lesson> entity2 = new HttpEntity<>(lesson, getHttpHeader());
		
		ResponseEntity<String> response2 = restTemplate.exchange(
				createURLWithPort("/api/instructors/1/lessons"),
				HttpMethod.POST, entity2, String.class);
		
		HttpEntity<Student> entity3 = new HttpEntity<>(student, getHttpHeader());
		
		ResponseEntity<String> response3 = restTemplate.exchange(
				createURLWithPort("/api/lessons/1/students"),
				HttpMethod.POST, entity3, String.class);
		
		
		//String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);
		
		assertEquals(HttpStatus.CREATED.value(), response2.getStatusCode().value());
		//assertTrue(actual.contains("/instructors/10001/lessons/SpringBoot"));
	}
	
	@Test
	@Order(2)
	public void updatelesson() throws JSONException{
		Instructor instructor = new Instructor(1, "instrcutor1","address1");
		Lesson lesson = new Lesson(1,"Spring Boot Introduction updated",  "SpringBoot");
		lesson.setInstructor(instructor);

		HttpEntity<Lesson> entity = new HttpEntity<>(lesson, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/lessons/1"),
				HttpMethod.PUT, entity, String.class);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		
		String expected = "{\"id\":1,\"description\":\"Spring Boot Introduction updated\",\"lessonname\":\"SpringBoot\"}";
		
		JSONAssert.assertEquals(expected, response.getBody(), false);
		
	}
	
	@Test
	@Order(3)
	public void testGetLesson() throws JSONException, JsonProcessingException{
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				createURLWithPort("/api/lessons/1/SpringBoot"),
				HttpMethod.GET, entity, String.class);
		
		String expected = "{\"id\":1,\"description\":\"Spring Boot Introduction updated\",\"lessonname\":\"SpringBoot\"}";
		
		JSONAssert.assertEquals(expected, response1.getBody(), false);
	}
	
	@Test
	@Order(4)
	public void testGetAllLessonsByInstructorId() throws JSONException, JsonProcessingException{
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				createURLWithPort("/api/instructors/1/lessons"),
				HttpMethod.GET, entity, String.class);
		
		String expected = "[{\"id\":1,\"description\":\"Spring Boot Introduction updated\",\"lessonname\":\"SpringBoot\",\"student\":[{\"id\":1,\"studentname\":\"student1\",\"contact\":\"contact1\"}]}]";
		
		JSONAssert.assertEquals(expected, response1.getBody(), false);
	}
	
	
	@Test
	@Order(5)
	public void testDeleteLesson() {
		Lesson lesson = restTemplate.getForObject(createURLWithPort("/api/lessons/1"), Lesson.class);
		assertNotNull(lesson);
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/lessons/1"),
				HttpMethod.DELETE, entity, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
		
		try {
			lesson = restTemplate.getForObject("/api/lessons/1", Lesson.class);
		}catch(LessonNotFoundException e){
			assertEquals("Lesson id not found : 1", e.getMessage());
		}
	}
	
	@Test
	@Order(6)
	public void testDeleteLessonByInstructorId() {
		Lesson lesson = restTemplate.getForObject(createURLWithPort("/api/lessons/1"), Lesson.class);
		Instructor instructor = restTemplate.getForObject(createURLWithPort("/api/instructors/1"), Instructor.class);
		assertNotNull(lesson);
		assertNotNull(instructor);
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/instructors/1/lessons"),
				HttpMethod.DELETE, entity, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
		
		try {
			lesson = restTemplate.getForObject("/api/lessons/1", Lesson.class);
			instructor = restTemplate.getForObject(createURLWithPort("/api/instructors/1"), Instructor.class);
		}catch(LessonNotFoundException e){
			assertEquals("Lesson id not found : 1", e.getMessage());
			assertEquals("Instructor id not found : 1", e.getMessage());
		}
	}
	
	private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
	
	

