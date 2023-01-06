package com.sideproject1.repository;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sideproject1.model.Lesson;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long>, JpaSpecificationExecutor<Lesson>{
	List<Lesson> findByLessonname(String lessonname);
//	List<Lesson> findByInstructornameContaining(String instructorname);
//	List<Lesson> findByLessonnameContaining(String lessonname);
	
	List<Lesson> findByInstructorId(Long instructorid);
	
	List<Lesson> findLessonsByStudentsId(Long studentId);
	
	@Transactional
	List<Lesson> deleteByInstructorId(long instructorId);
	
//	@Transactional
//	void deleteByStudentId(long studentId);
}