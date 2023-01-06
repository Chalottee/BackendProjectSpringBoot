package com.sideproject1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sideproject1.exception.InstructorNotFoundException;
import com.sideproject1.model.Instructor;
import com.sideproject1.repository.InstructorRepository;
import com.sideproject1.service.impl.InstructorServiceImpl;

@ExtendWith(SpringExtension.class)
public class InstructorServiceMockTest {

	@Mock
	private InstructorRepository instructorRepository;
	
	@InjectMocks
	private InstructorService instructorService = new InstructorServiceImpl();
	
	@Test
	public void getAllInstructors() {
		List<Instructor> instructors = Arrays.asList(
				new Instructor(10001, "Instructor1", "Address1"),
				new Instructor(10002, "Instructor2", "Address2"));
		
		when(instructorRepository.findAll()).thenReturn(instructors);
		assertEquals(instructors, instructorService.getAllInstructors());
				
	}
	
	@Test
	public void getInstructorById(){
		Instructor instructor = new Instructor(10001, "Instructor1", "Address1");
		
		when(instructorRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(instructor));
		assertEquals(instructor, instructorService.getInstructorById(Long.valueOf(10001)));
	}
	
	@Test
	public void getInstructorNotFound(){
		
		InstructorNotFoundException exception = assertThrows(
				InstructorNotFoundException.class,
					() -> instructorService.getInstructorById( Long.valueOf(10001)),
					"Instructor id not found :10001"
				);
				
		assertEquals("Instructor id not found : 10001", exception.getMessage());
	}
	
	@Test
	public void createInstructor() {
		Instructor instructor = new Instructor(10001, "Instructor1", "Address1");
		
		when(instructorRepository.save(instructor)).thenReturn(instructor);
		assertEquals(instructor, instructorService.createInstructor(instructor, Long.valueOf(10001)));
	}
	
	@Test
	public void updateInstructor() {
		Instructor instructor = new Instructor(10001, "Instructor1 updated", "Address1 updated");
		
		when(instructorRepository.save(instructor)).thenReturn(instructor);
		assertEquals(instructor, instructorService.updateInstructor(Long.valueOf(10001), instructor));
	}
	
	@Test
	public void deleteInstructor() {
		
		Instructor instructor = new Instructor(10001, "Instructor1", "Address1");
		
		when(instructorRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(instructor));
		instructorService.deleteInstructor(Long.valueOf(10001));
		
		verify(instructorRepository, times(1)).deleteById(Long.valueOf(10001));
	}
}
