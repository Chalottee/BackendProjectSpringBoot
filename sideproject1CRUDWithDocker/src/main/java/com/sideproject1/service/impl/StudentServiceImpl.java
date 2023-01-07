package com.sideproject1.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.sideproject1.exception.StudentNotFoundException;
import com.sideproject1.model.Lesson;
import com.sideproject1.model.Student;
import com.sideproject1.repository.LessonRepository;
import com.sideproject1.repository.StudentRepository;
import com.sideproject1.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService{

 Logger logger = LoggerFactory.getLogger(StudentService.class);
	
    @Autowired 
    private StudentRepository studentRepository;
    
    @Autowired
    private LessonRepository lessonRepository;
    
    @Override
	public List<Student> getAllStudents(){
        logger.trace("Entered getAllStudents method");
   		
   		List<Student> students = studentRepository.findAll();

		return students;
    }
	
    @Override
	public Student getStudentById(long id) {
    	return studentRepository.findById(id).orElseThrow(() -> new StudentNotFoundException(id));
    }
    
    @Override
    public List<Student> getAllStudentsByLessonId(long lessonId){
    	if (!lessonRepository.existsById(lessonId)) {
  	      throw new ResourceNotFoundException("Not found Lesson with id = " + lessonId);
  	    }
    	
    	return studentRepository.findStudentsByLessonsId(lessonId);
    }
	
    
    @Override
    public List<Lesson> getAllLessonsByStudentId(long studentId){
    	if (!studentRepository.existsById(studentId)) {
		      throw new ResourceNotFoundException("Not found Student  with id = " + studentId);
		    }
    	
    	return lessonRepository.findLessonsByStudentsId(studentId);
    }
    
    @Override
    public Student addStudent(long lessonId, Student studentRequest) {

    	Student student = lessonRepository.findById(lessonId).map(lesson -> {
  	      Long studentId = studentRequest.getId();
  	      
  	      // student is existed
  	      if (studentId != null) {
  	        Student _student = studentRepository.findById(studentId)
  	            .orElseThrow(() -> new ResourceNotFoundException("Not found Student with id = " + studentId));
  	        lesson.addStudent(_student);
  	        lessonRepository.save(lesson);
  	        return _student;
  	      }
  	      
  	      // add and create new student
  	      lesson.addStudent(studentRequest);
  	      return studentRepository.save(studentRequest);
  	    }).orElseThrow(() -> new ResourceNotFoundException("Not found Lesson with id = " + lessonId));

		return student;
    }
    
    @Override
	public Student updateStudent(long id, Student student) {
    	
    	Student updatedStudent = studentRepository.save(student);
		return updatedStudent;
    }
	
//    @Override
//	public void deleteStudentById(Long id) {
//    	Optional<Student> student = studentRepository.findById(id);
//		if(student.isPresent()) {
//			studentRepository.deleteById(id);
//		} else {
//			throw new StudentNotFoundException(id);
//		}
//    }
    
    @Override
    public void deleteStudentFromLesson(long lessonId, long studentId) {
    	Lesson lesson = lessonRepository.findById(lessonId)
    	        .orElseThrow(() -> new ResourceNotFoundException("Not found Lesson with id = " + lessonId));
    	    
    	    lesson.removeStudent(studentId);
    	    lessonRepository.save(lesson);
    }
}
