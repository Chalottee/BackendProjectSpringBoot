package com.sideproject1.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sideproject1.model.Lesson;
import com.sideproject1.model.Student;
//import com.sideproject1.repository.LessonRepository;
import com.sideproject1.repository.StudentRepository;
import com.sideproject1.service.StudentService;

@RestController
public class StudentController {

	@Autowired
	private StudentService studentService;
	
//	@Autowired
//	private LessonRepository lessonRepository;
//	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("/students")
	public List<Student> getAllStudents(){
		List<Student> students = studentService.getAllStudents();
		
		return students;
	}
	
	@GetMapping("/students/{id}")
	public Student getStudentById(@PathVariable long id) {
		Student student = studentService.getStudentById(id);
		
		return student;
	}
	
	@GetMapping("/lessons/{lessonId}/students")
	public ResponseEntity<List<Student>> getAllStudentsByLessonId(@PathVariable(value = "lessonId") long lessonId) {

	    List<Student> students = studentService.getAllStudentsByLessonId(lessonId);
	    return new ResponseEntity<>(students, HttpStatus.OK);
	  }
	
	@GetMapping("/students/{studentId}/lessons")
	public ResponseEntity<List<Lesson>> getAllLessonsByStudentId(@PathVariable(value = "studentId") long studentId){

		    List<Lesson> lessons = studentService.getAllLessonsByStudentId(studentId);
		    return new ResponseEntity<>(lessons, HttpStatus.OK);
		  }
	
	@PostMapping("/lessons/{lessonId}/students")
	  public ResponseEntity<Student> addStudent(@PathVariable(value = "lessonId") long lessonId, @RequestBody Student studentRequest) {
	    Student student = studentService.addStudent(lessonId, studentRequest);
	    return new ResponseEntity<>(student, HttpStatus.CREATED);
	  }
	
	@PutMapping("/students/{id}")
	public ResponseEntity<Student> updateStudent(@PathVariable long id,@RequestBody Student studentRequest){
		
		Student student = studentRepository.findById(id)
		        .orElseThrow(() -> new ResourceNotFoundException("StudentId " + id + "not found"));

		    student.setContact(studentRequest.getContact());
		    student.setStudentname(studentRequest.getStudentname());

		    Student studentUpdated = studentService.updateStudent(id, student);
			
			ResponseEntity<Student> responseEntity = new ResponseEntity<Student>(studentUpdated, HttpStatus.OK);

			return responseEntity;
		    
	}
	
//	@DeleteMapping("/students/{id}")
//	public ResponseEntity<Void> deleteStudentById(@PathVariable Long id){
//		studentService.deleteStudentById(id);
//		
//		ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
//		return responseEntity;
//	}
	
	@DeleteMapping("/lessons/{lessonId}/students/{studentId}")
	  public ResponseEntity<HttpStatus> deleteStudentFromLesson(@PathVariable(value = "lessonId") long lessonId, @PathVariable(value = "studentId") long studentId) {
	    studentService.deleteStudentFromLesson(lessonId, studentId);
	    
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	  }
}
