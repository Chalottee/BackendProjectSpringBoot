package com.sideproject1.service.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.sideproject1.exception.LessonNotFoundException;
import com.sideproject1.model.Lesson;
import com.sideproject1.repository.InstructorRepository;
import com.sideproject1.repository.LessonRepository;
import com.sideproject1.service.LessonService;

@Service
public class LessonServiceImpl implements LessonService {
	
	Logger logger = LoggerFactory.getLogger(LessonServiceImpl.class);
	
    @Autowired 
    private LessonRepository lessonRepository;
    @Autowired
    private InstructorRepository instructorRepository;

   	@Override
	public List<Lesson> getAllLessons() {
   		logger.trace("Entered getAlllessons method");
   		
   		List<Lesson> lessons = lessonRepository.findAll();

		return lessons;
	}
   	
   	@Override
   	public List<Lesson> getAllLessonsByInstructorId(long instructorId){
   		if (!instructorRepository.existsById(instructorId)) {
   	      throw new ResourceNotFoundException("Not found Instructor with id = " + instructorId);
   	    }

   	    List<Lesson> lessons = lessonRepository.findByInstructorId(instructorId);
   	    return lessons;
   	  }
   	

	@Override
	public Lesson getLesson(long id, String lessonname) {
		return lessonRepository.findById(id).orElseThrow(() -> new LessonNotFoundException(id));
	}

	@Override
	public void deleteLesson(long id) {
		Optional<Lesson> lesson = lessonRepository.findById(id);
		if(lesson.isPresent()) {
			lessonRepository.deleteById(id);
		} else {
			throw new LessonNotFoundException(id);
		}
		
	}
	
	@Override
	public List<Lesson> deleteLessonByInstructorId(long instructorId) {
		if (!instructorRepository.existsById(instructorId)) {
		      throw new ResourceNotFoundException("Not found Instructor with id = " + instructorId);
		    }

			return lessonRepository.deleteByInstructorId(instructorId);
	}


	@Override
	public Lesson updateLesson(long id, Lesson lessonRequest) {

		
		Lesson lessonUpdated = lessonRepository.save(lessonRequest);
		return lessonUpdated;
	}

	@Override
	public Lesson createLesson(Lesson lessonRequest, long instructorId) {
		
		Lesson lesson = instructorRepository.findById(instructorId).map(instructor -> {
		      lessonRequest.setInstructor(instructor);
		      return lessonRepository.save(lessonRequest);
		      
		    }).orElseThrow(() -> new ResourceNotFoundException("Not found Instrcutor with id = " + instructorId));

		
		return lesson;
		
	}
	
//	@Override
//	public List<Lesson> findByInstructornameContaining(String instructorname){
//		if(instructorname == null) {
//			List<Lesson> lessons = lessonRepository.findAll();
//			return lessons;
//		}else {
//			List<Lesson> lessons = lessonRepository.findByInstructornameContaining(instructorname);
//			return lessons;
//		}
//	}
	
//	@Override
//	public List<Lesson> findByLessonnameContaining(String lessonname){
//		if(lessonname == null) {
//			List<Lesson> lessons = lessonRepository.findAll();
//			return lessons;
//		}else {
//			List<Lesson> lessons = lessonRepository.findByLessonnameContaining(lessonname);
//			return lessons;
//		}
//	}
    
}
