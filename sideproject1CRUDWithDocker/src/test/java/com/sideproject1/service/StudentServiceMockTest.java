package com.sideproject1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.sideproject1.exception.StudentNotFoundException;
import com.sideproject1.model.Instructor;
import com.sideproject1.model.Lesson;
import com.sideproject1.model.Student;
import com.sideproject1.repository.LessonRepository;
import com.sideproject1.repository.StudentRepository;
import com.sideproject1.service.impl.StudentServiceImpl;

@ExtendWith(SpringExtension.class)
public class StudentServiceMockTest {

	@Mock
	private StudentRepository studentRepository;
	
	@Mock
	private LessonRepository lessonRepository;
	
	@InjectMocks
	private StudentService studentService = new StudentServiceImpl();
	
	@Test
	public void getAllStudents() {
		//Instructor instructor1 = new Instructor(10001, "instrcutor1","address1");
		//Instructor instructor2 = new Instructor(10002, "instrcutor2","address2");
		List<Student> students = Arrays.asList(
				new Student(10001, "Student1", "123456"),
				new Student(10002, "student2", "123567"));
		
		when(studentRepository.findAll()).thenReturn(students);
		assertEquals(students, studentService.getAllStudents());
				
	}
	
	@Test
	public void getStudentById(){
		//Instructor instructor1 = new Instructor(10001, "instrcutor1","address1");
		Student student = new Student(10001, "Student1", "123456");
		
		when(studentRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(student));
		assertEquals(student, studentService.getStudentById(Long.valueOf(10001)));
	}
	
	
	@Test
	public void getAllStudentsByLessonId() {
		Set<Student> students = new HashSet<>();
		Student student = new Student(10001, "Student1", "123456");
		students.add(student);
		Lesson lesson = new Lesson(10001, "Learn Java", "Java");
		lesson.setStudent(students);

		List<Student> arr = new ArrayList<>(students);

		when(lessonRepository.existsById(Long.valueOf(10001))).thenReturn(true);
		when(studentRepository.findStudentsByLessonsId(Long.valueOf(10001))).thenReturn(arr);
		assertEquals(arr, studentService.getAllStudentsByLessonId(Long.valueOf(10001)));
		
	}
	
	@Test
	public void getAllLessonsByStudentId() {
		Set<Lesson> lessons = new HashSet<>();
		Lesson lesson = new Lesson(10001, "Learn Java", "Java");
		lessons.add(lesson);
		Student student = new Student(10001, "Student1", "123456");
		student.setLessons(lessons);

		List<Lesson> arr2 = new ArrayList<>(lessons);
		when(studentRepository.existsById(Long.valueOf(10001))).thenReturn(true);
		when(lessonRepository.findLessonsByStudentsId(Long.valueOf(10001))).thenReturn(arr2);
		assertEquals(arr2, studentService.getAllLessonsByStudentId(Long.valueOf(10001)));
		
	}
	
	@Test
	public void getStudentNotFound(){
		
		StudentNotFoundException exception = assertThrows(
				StudentNotFoundException.class,
					() -> studentService.getStudentById( Long.valueOf(10001)),
					"Student id not found :10001"
				);
				
		assertEquals("Student id not found : 10001", exception.getMessage());
	}
	
	@Test
    public void updateStudent() {
		Student student = new Student(10001, "Student3", "654321");
    	
    	when(studentRepository.save(student)).thenReturn(student);
		assertEquals(student, studentService.updateStudent( Long.valueOf(10001), student));
    }
	
//	@Test
//	public void deleteStudentById() {
//		
//		//Instructor instructor1 = new Instructor(10001, "instrcutor1","address1");
//		Student student = new Student(10001, "Student1", "123456");
//		
//		when(studentRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(student));
//		studentService.deleteStudentById(Long.valueOf(10001));
//		
//		verify(studentRepository, times(1)).deleteById(Long.valueOf(10001));
//	}
//	
	
	@Test
	public void deleteStudentFromLesson(){
		Set<Student> students = new HashSet<>();
		Student student = new Student(10001, "Student1", "123456");
		students.add(student);
		Lesson lesson = new Lesson(10001, "Learn Java", "Java");
		lesson.setStudent(students);
		
		when(lessonRepository.findById(Long.valueOf(10001))).thenReturn(Optional.of(lesson));
		studentService.deleteStudentFromLesson(Long.valueOf(10001), Long.valueOf(10001));
		
	
		verify(lessonRepository, times(1)).save(lesson);
	}
}
