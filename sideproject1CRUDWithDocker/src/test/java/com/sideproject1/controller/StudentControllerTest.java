package com.sideproject1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sideproject1.model.Lesson;
import com.sideproject1.model.Student;
import com.sideproject1.repository.StudentRepository;
import com.sideproject1.service.StudentService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class StudentControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StudentRepository studentRepository;
	
	@MockBean
	private StudentService studentService;
	
	private static final ObjectMapper om = new ObjectMapper();
	
	Lesson mockLesson = new Lesson(1,"Spring Boot Introduction","SpringBoot");
	Student mockStudent = new Student(1,"student1","contact1");
	
	List<Student> mockstudents = new ArrayList<>();
	List<Lesson> mocklessons = new ArrayList<>();
	
	String exampleStudentJson = "{\"id\":1,\"studentname\":\"student1\",\"contact\":\"contact1\"}";

	@Test
	public void getStudentById() throws Exception{
		
		Mockito.when(studentService.getStudentById(Long.valueOf(1))).thenReturn(mockStudent);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/students/1").accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		//JSONAssert.assertEquals(exampleLessonJson, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void getAllStudentsByLessonId() throws Exception{
		mocklessons.add(mockLesson);
		mockStudent.setLessons(mocklessons.stream().collect(Collectors.toSet()));
		mockstudents.add(mockStudent);
		
		Mockito.when(studentService.getAllStudentsByLessonId(Long.valueOf(1))).thenReturn(mockstudents);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/lessons/1/students").accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void getAllLessonsByStudentId() throws Exception{
		mockstudents.add(mockStudent);
		mockLesson.setStudent(mockstudents.stream().collect(Collectors.toSet()));
		mocklessons.add(mockLesson);
		
		Mockito.when(studentService.getAllLessonsByStudentId(Long.valueOf(1))).thenReturn(mocklessons);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/students/1/lessons").accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void addStudent() throws Exception{
		mocklessons.add(mockLesson);
		mockStudent.setLessons(mocklessons.stream().collect(Collectors.toSet()));
		
		//Mockito.when(instructorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(instructor));
		Mockito.when(studentService.addStudent(Long.valueOf(Mockito.anyLong()), Mockito.any(Student.class))).thenReturn(mockStudent);
		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/lessons/1/students").content(exampleStudentJson).contentType(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
	}
	
	@Test
	public void updateStudent() throws Exception {
				
		Student student = new Student(1,"student1","contact1 updated");
		
		Mockito.when(studentRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(student));
		Mockito.when(studentService.updateStudent(Long.valueOf(Mockito.anyLong()), Mockito.any(Student.class))).thenReturn(student);
		
		String studentString = om.writeValueAsString(student);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
				"/students/1").contentType(
				MediaType.APPLICATION_JSON).content(studentString);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		//JSONAssert.assertEquals(exampleLessonJson, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void deleteStudentFromLesson() throws Exception{
		mockstudents.add(mockStudent);
		mockLesson.setStudent(mockstudents.stream().collect(Collectors.toSet()));
		mocklessons.add(mockLesson);
		doNothing().when(studentService).deleteStudentFromLesson(Long.valueOf(1),Long.valueOf(1));		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/lessons/1/students/1");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
	}
}
