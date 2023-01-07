package com.sideproject1.service;

import java.util.List;

import com.sideproject1.model.Lesson;

public interface LessonService {

	List<Lesson> getAllLessons();
	
	List<Lesson> getAllLessonsByInstructorId(long instructorId);
	
	Lesson getLesson(long id, String lessonname);
	
	void deleteLesson(long id);
	
	List<Lesson> deleteLessonByInstructorId(long instructorId);
	
	Lesson updateLesson( long id, Lesson lessonRequest);
	
	Lesson createLesson(Lesson lessonRequest, long instructorId);
	
//	List<Lesson> findByInstructornameContaining(String instructorname);
	
//	List<Lesson> findByLessonnameContaining(String lessonname);
}
