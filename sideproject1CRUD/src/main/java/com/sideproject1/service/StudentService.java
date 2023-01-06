package com.sideproject1.service;

import java.util.List;

import com.sideproject1.model.Lesson;
import com.sideproject1.model.Student;


public interface StudentService {

	List<Student> getAllStudents();
	
	Student getStudentById(long id);
	
	
	List<Student> getAllStudentsByLessonId(long lessonId);
	
	List<Lesson>  getAllLessonsByStudentId(long studentId);
	
	Student addStudent(long lessonId, Student studentRequest);
	
	Student updateStudent(long id, Student student);
	
	//void deleteStudentById(Long id);
	
	void deleteStudentFromLesson(long lessonId, long studentId);
}
