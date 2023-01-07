package com.sideproject1.service;

import java.util.List;

import com.sideproject1.model.Instructor;

public interface InstructorService {

	List<Instructor> getAllInstructors();
	
	Instructor getInstructorById(Long id);
	
	Instructor createInstructor(Instructor instructor,Long id);
	
	Instructor updateInstructor(Long id, Instructor instructor);
	
	void deleteInstructor(Long id);
	
	void deleteAllInstructors();
	
}
