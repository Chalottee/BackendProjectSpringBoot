package com.sideproject1.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sideproject1.exception.InstructorNotFoundException;
import com.sideproject1.model.Instructor;
import com.sideproject1.repository.InstructorRepository;
import com.sideproject1.service.InstructorService;

@Service
public class InstructorServiceImpl implements InstructorService{

	Logger logger = LoggerFactory.getLogger(InstructorService.class);
	
    @Autowired 
    private InstructorRepository instructorRepository;
    
    @Override
    public List<Instructor> getAllInstructors(){
    	logger.trace("Entered getAllInstructors method");
   		
   		List<Instructor> instructors = instructorRepository.findAll();

		return instructors;
    }
	
    @Override
	public Instructor getInstructorById(Long id) {
    	return instructorRepository.findById(id).orElseThrow(() -> new InstructorNotFoundException(id));
    }
	
    @Override
	public Instructor createInstructor(Instructor instructor,Long id) {
    	Instructor createdinstructor = instructorRepository.save(instructor);
		return createdinstructor;
		
    }
	
    @Override
	public Instructor updateInstructor(Long id, Instructor instructor) {
    	Instructor updatedinstructor = instructorRepository.save(instructor);
		return updatedinstructor;
    }
	
    @Override
	public void deleteInstructor(Long id) {
    	Optional<Instructor> instructor = instructorRepository.findById(id);
		if(instructor.isPresent()) {
			instructorRepository.deleteById(id);
		} else {
			throw new InstructorNotFoundException(id);
		}
    }
	
    @Override
	public void deleteAllInstructors() {
			instructorRepository.deleteAll();
		
    }
	

}
