package com.sideproject1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
public class StudentControllerIntegrationTest extends BaseIntegrationTest{

	@LocalServerPort
	private int port;
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Test
	@Order(1)
	public void addstudent() {
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
		
		assertEquals(HttpStatus.CREATED.value(), response3.getStatusCode().value());
	}
	
	@Test
	@Order(2)
	public void updatestudent() throws JSONException{
		Lesson lesson = new Lesson(1,"Spring Boot Introduction","SpringBoot");
		Student student = new Student(1,"student1","contact1 updated");
		List<Student> students = new ArrayList<>();
		Instructor instructor = new Instructor(1, "instrcutor1","address1");
		lesson.setInstructor(instructor);
		List<Lesson> lessons = new ArrayList<>();
		lessons.add(lesson);
		students.add(student);
		student.setLessons(lessons.stream().collect(Collectors.toSet()));
		lesson.setStudent(students.stream().collect(Collectors.toSet()));
		

		HttpEntity<Student> entity = new HttpEntity<>(student, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/students/1"),
				HttpMethod.PUT, entity, String.class);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
		
		String expected = "{\"id\":1,\"studentname\":\"student1\",\"contact\":\"contact1 updated\"}";
		
		JSONAssert.assertEquals(expected, response.getBody(), false);
		
	}
	
	@Test
	@Order(3)
public void testgetStudentById() throws JSONException, JsonProcessingException{
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				createURLWithPort("/api/students/1"),
				HttpMethod.GET, entity, String.class);
		
//		String expected = "{\"id\":1,\"studentname\":\"student1\",\"contact\":\"contact1\"}";
//		
//		JSONAssert.assertEquals(expected, response1.getBody(), false);
		
		assertEquals(HttpStatus.OK.value(), response1.getStatusCode().value());

	}
	
	@Test
	@Order(4)
	public void testgetAllStudentsByLessonId() throws JSONException, JsonProcessingException{
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				createURLWithPort("/api/lessons/1/students"),
				HttpMethod.GET, entity, String.class);
		
//		String expected = "[{\"id\":1,\"studentname\":\"student1\",\"contact\":\"contact1\"}]";
//		
//		JSONAssert.assertEquals(expected, response1.getBody(), false);
		
		assertEquals(HttpStatus.OK.value(), response1.getStatusCode().value());

	}
	
	@Test
	@Order(5)
    public void testgetAllLessonsByStudentId() throws JSONException, JsonProcessingException{
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response1 = restTemplate.exchange(
				createURLWithPort("/api/students/1/lessons"),
				HttpMethod.GET, entity, String.class);
		
//		String expected = "[{\"id\":1,\"studentname\":\"student1\",\"contact\":\"contact1\"}]";
//		
//		JSONAssert.assertEquals(expected, response1.getBody(), false);
		
		assertEquals(HttpStatus.OK.value(), response1.getStatusCode().value());

	}
	
	@Test
	@Order(6)
	public void testdeleteStudentFromLesson() {
		Lesson lesson = restTemplate.getForObject(createURLWithPort("/api/lessons/1"), Lesson.class);
		Student student = restTemplate.getForObject(createURLWithPort("/api/students/1"), Student.class);
		assertNotNull(lesson);
		assertNotNull(student);
		
		HttpEntity<String> entity = new HttpEntity<>(null, getHttpHeader());
		
		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/api/lessons/1/students/1"),
				HttpMethod.DELETE, entity, String.class);
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatusCode().value());
		
		try {
			lesson = restTemplate.getForObject("/api/lessons/1", Lesson.class);
			student = restTemplate.getForObject(createURLWithPort("/api/students/1"), Student.class);
		}catch(LessonNotFoundException e){
			assertEquals("Lesson id not found : 1", e.getMessage());
			assertEquals("Student id not found : 1", e.getMessage());
		}
	}
	
	
	
	private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}
