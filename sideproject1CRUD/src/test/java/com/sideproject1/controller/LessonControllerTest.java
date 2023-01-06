package com.sideproject1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import java.util.ArrayList;
import java.util.List;

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
import com.sideproject1.model.Instructor;
import com.sideproject1.model.Lesson;
import com.sideproject1.repository.InstructorRepository;
import com.sideproject1.repository.LessonRepository;
import com.sideproject1.service.LessonService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = LessonController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class LessonControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private LessonService lessonService;
	
	@MockBean
	private InstructorRepository instructorRepository;
	
	@MockBean
	private LessonRepository lessonRepository;
	
	@MockBean
	private LessonController lessonController;
	
	
	
	private static final ObjectMapper om = new ObjectMapper();
	
	//TODO move to base class as sample date
	Instructor mockInstructor = new Instructor(1, "instrcutor1","address1");
	Lesson mockLesson = new Lesson(1,"Spring Boot Introduction","SpringBoot");
	
	List<Lesson> mockLessons = new ArrayList<>();
	
	
	
	String exampleLessonJson = "{\"id\":1,\"description\":\"Spring Boot Introduction\",\"lessonname\":\"SpringBoot\"}";
	//String exampleLessonJson2 = "{\"id\":10001,\"description\":\"Spring Boot Introduction\",\"lessonname\":\"SpringBoot\",\"instructor_id\":1}";
	
	@Test
	public void getLesson() throws Exception{
		
		Mockito.when(lessonService.getLesson(1,"SpringBoot")).thenReturn(mockLesson);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/lessons/1/SpringBoot").accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		//JSONAssert.assertEquals(exampleLessonJson, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void getAllLessonsByInstructorId() throws Exception{
		Lesson lesson = new Lesson(1, "Spring Boot Introduction","SpringBoot");
		Instructor instructor = new Instructor(1, "instructor1", "address1");
		lesson.setInstructor(instructor);
		mockLessons.add(lesson);
		Mockito.when(lessonService.getAllLessonsByInstructorId(1)).thenReturn(mockLessons);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/instructors/1/lessons").accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void createLesson() throws Exception{
		
		Lesson lesson = new Lesson(1, "Spring Boot Introduction","SpringBoot");
		Instructor instructor = new Instructor(1, "instructor1", "address1");
		lesson.setInstructor(instructor);
		
		//Mockito.when(instructorRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(instructor));
		Mockito.when(lessonService.createLesson(Mockito.any(Lesson.class),Mockito.anyLong())).thenReturn(lesson);
		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/instructors/1/lessons").content(exampleLessonJson).contentType(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		

	}
	
	@Test
	public void updateLesson() throws Exception {
		Lesson lesson = new Lesson(1, "Spring Boot Introduction updated","SpringBoot");
		Instructor instructor = new Instructor(1, "instructor1", "address1");
		lesson.setInstructor(instructor);		
		
		Mockito.when(lessonService.updateLesson(Mockito.anyLong(), Mockito.any(Lesson.class))).thenReturn(lesson);
		
		String lessonString = om.writeValueAsString(lesson);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
				"/lessons/1").contentType(
				MediaType.APPLICATION_JSON).content(lessonString);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		//JSONAssert.assertEquals(exampleLessonJson, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void deleteLesson() throws Exception{
		doNothing().when(lessonService).deleteLesson(Long.valueOf(1));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/lessons/1");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
	
	@Test
	public void deleteLessonByInstructorId() throws Exception{
		Lesson lesson = new Lesson(1, "Spring Boot Introduction","SpringBoot");
		Instructor instructor = new Instructor(1, "instructor1", "address1");
		lesson.setInstructor(instructor);
		mockLessons.add(lesson);
		Mockito.when(lessonService.deleteLessonByInstructorId(1)).thenReturn(mockLessons);
		
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/instructors/1/lessons");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
	}
}
