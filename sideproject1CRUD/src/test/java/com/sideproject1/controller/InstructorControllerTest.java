package com.sideproject1.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
import com.sideproject1.service.InstructorService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = InstructorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class InstructorControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private InstructorService instructorService;
	
	private static final ObjectMapper om = new ObjectMapper();
	
	//TODO move to base class as sample date
	Instructor mockInstructor = new Instructor(10001, "instrcutor1","address1");
	
	String exampleInstructorJson = "{\"id\":10001,\"instructorname\":\"instrcutor1\",\"address\":\"address1\"}";
	
	@Test
	public void getInstructorById() throws Exception{
		
		Mockito.when(instructorService.getInstructorById(Long.valueOf(10001))).thenReturn(mockInstructor);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/instructors/10001").accept(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		JSONAssert.assertEquals(exampleInstructorJson, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void createInstructor() throws Exception{
		
		Instructor instructor = new Instructor(10001, "instrcutor1","address1");
		
		Mockito.when(instructorService.createInstructor(Mockito.any(Instructor.class), Mockito.anyLong())).thenReturn(instructor);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
				"/instructors/10001").content(exampleInstructorJson).contentType(
				MediaType.APPLICATION_JSON);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		assertEquals("http://localhost/instructors/10001",
				response.getHeader(HttpHeaders.LOCATION));
	}
	
	@Test
	public void updateInstructor() throws Exception {
		Instructor instructor = new Instructor(10001, "instrcutor1","address1");
		
		Mockito.when(instructorService.updateInstructor(Mockito.anyLong(), Mockito.any(Instructor.class))).thenReturn(instructor);
		
		String instructorString = om.writeValueAsString(instructor);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put(
				"/instructors/10001").contentType(
				MediaType.APPLICATION_JSON).content(instructorString);
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.OK.value(), response.getStatus());
		
		JSONAssert.assertEquals(exampleInstructorJson, result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void deleteInstructor() throws Exception{
		doNothing().when(instructorService).deleteInstructor(Long.valueOf(10001));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete(
				"/instructors/10001");
		
		MvcResult result = mockMvc.perform(requestBuilder).andReturn();
		
		MockHttpServletResponse response = result.getResponse();
		
		assertEquals(HttpStatus.NO_CONTENT.value(), response.getStatus());
	}
}
