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

import com.sideproject1.exception.LessonNotFoundException;
import com.sideproject1.model.Instructor;
import com.sideproject1.model.Lesson;
import com.sideproject1.repository.InstructorRepository;
import com.sideproject1.repository.LessonRepository;
import com.sideproject1.service.impl.LessonServiceImpl;

@ExtendWith(SpringExtension.class)
public class LessonServiceMockTest {

	@Mock
	private LessonRepository lessonRepository;
	
	@Mock
	private InstructorRepository instructorRepository;
	
	@InjectMocks
	private LessonService lessonService = new LessonServiceImpl();
	
	@Test
	public void getAllLessons() {
		//Instructor instructor1 = new Instructor(10001, "instrcutor1","address1");
		//Instructor instructor2 = new Instructor(10002, "instrcutor2","address2");
		List<Lesson> lessons = Arrays.asList(
				new Lesson(10001, "Learn Java", "Java"),
				new Lesson(10002, "Learn Spring Boot", "SpringBoot"));
		
		when(lessonRepository.findAll()).thenReturn(lessons);
		assertEquals(lessons, lessonService.getAllLessons());
				
	}
	
	@Test
	public void getLesson(){
		//Instructor instructor1 = new Instructor(10001, "instrcutor1","address1");
		Lesson lesson = new Lesson(10001, "Learn Java", "Java");
		
		when(lessonRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(lesson));
		assertEquals(lesson, lessonService.getLesson(Long.valueOf(10001), "Java"));
	}
	
	@Test
	public void getLessonNotFound(){
		
		LessonNotFoundException exception = assertThrows(
				LessonNotFoundException.class,
					() -> lessonService.getLesson( Long.valueOf(10001), "Java"),
					"Lesson id not found :10001"
				);
				
		assertEquals("Lesson id not found : 10001", exception.getMessage());
	}
	
	@Test
	public void deleteLesson() {
		
		//Instructor instructor1 = new Instructor(10001, "instrcutor1","address1");
		Lesson lesson = new Lesson(10001, "Learn Java", "Java");
		
		when(lessonRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(lesson));
		lessonService.deleteLesson(Long.valueOf(10001));
		
		verify(lessonRepository, times(1)).deleteById(Long.valueOf(10001));
	}
	

	@Test
	public void deleteLessonByInstructorId() {
		Instructor instructor = new Instructor(10001, "instrcutor1","address1");
		Lesson lesson = new Lesson(10001, "Learn Java", "Java");
		lesson.setInstructor(instructor);
		
		when(instructorRepository.existsById(Long.valueOf(10001))).thenReturn(true);
		when(lessonRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(lesson));
		lessonService.deleteLessonByInstructorId(Long.valueOf(10001));
		
		verify(lessonRepository, times(1)).deleteByInstructorId(Long.valueOf(10001));
		
	}
	
	@Test
	public void updateLesson() {
		Lesson lesson = new Lesson(10001, "in28minutes", "Learn Java");
    	
    	when(lessonRepository.save(lesson)).thenReturn(lesson);
		assertEquals(lesson, lessonService.updateLesson(Long.valueOf(10001), lesson));
	}
	
	@Test
	public void createLesson() {
		Instructor instructor = new Instructor(10001, "instrcutor1","address1");
		Lesson lesson = new Lesson(10001, "bytecaptain", "Learn Java");
    	lesson.setInstructor(instructor);
    	
    	when(instructorRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(instructor));
    	when(lessonRepository.save(lesson)).thenReturn(lesson);
		assertEquals(lesson, lessonService.createLesson(lesson,Long.valueOf(10001)));
	}
}
