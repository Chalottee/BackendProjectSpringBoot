package com.sideproject1.controller;

import java.net.URI;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sideproject1.model.Lesson;
import com.sideproject1.repository.InstructorRepository;
import com.sideproject1.repository.LessonRepository;
import com.sideproject1.service.LessonService;

//@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
@RestController
public class LessonController {
	
	//private final LessonRepository lessonRepository;
	
	//public LessonController(LessonRepository lessonRepository) {
		//this.lessonRepository = lessonRepository;
	//}
	
	//@GetMapping("/lessons")
	//public ResponseEntity<List<Lesson>> searchForLessons(@SearchSpec Specification<Lesson> specs){
		//return new ResponseEntity<>(lessonRepository.findAll(Specification.where(specs)), HttpStatus.OK);
	//}
	
//	@GetMapping("/lessons")
//	@ResponseBody
//	public List<Lesson> findByInstructornameContaining(@RequestParam(required = false) String instructorname){
//		List<Lesson> lessons = lessonService.findByInstructornameContaining(instructorname);
//		return lessons;
//	}
	
//	@GetMapping("/lessons")
//	@ResponseBody
//	public List<Lesson> findByLessonnameContaining(@RequestParam(required = false) String lessonname){
//		List<Lesson> lessons = lessonService.findByLessonnameContaining(lessonname);
//		return lessons;
//	}
	
	@Autowired
	private LessonService lessonService;
	
	@Autowired
	private InstructorRepository instructorRepository;
	@Autowired
	private LessonRepository lessonRepository;

	@GetMapping("/lessons")
	public List<Lesson> getAllLessons() {
		
		List<Lesson> lessons = lessonService.getAllLessons();
		
		return lessons;
	}
	
	@GetMapping("/lessons/{id}/{lessonname}")
	public Lesson getLesson(@PathVariable long id, @PathVariable String lessonname) {
		
		Lesson lesson = lessonService.getLesson(id, lessonname);
		return lesson;
	}
	
	@GetMapping("/instructors/{instructorId}/lessons")
	public List<Lesson> getAllLessonsByInstructorId(@PathVariable long instructorId){
		List<Lesson> lessons = lessonService.getAllLessonsByInstructorId(instructorId);
		
		return  lessons;
	}

	@DeleteMapping("/lessons/{id}")
	public ResponseEntity<Void> deleteLesson(@PathVariable long id) {

		lessonService.deleteLesson(id);

		ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
		return responseEntity;
	}
	
	@DeleteMapping("/instructors/{instructorId}/lessons")
	public ResponseEntity<Lesson> deleteLessonByInstructorId(@PathVariable long instructorId) {
		
		lessonService.deleteLessonByInstructorId(instructorId);
		
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PutMapping("/lessons/{id}")
	public ResponseEntity<Lesson> updateLesson(@PathVariable long id,
			@RequestBody Lesson lessonRequest) {

		 Lesson lesson = lessonRepository.findById(id)
			        .orElseThrow(() -> new ResourceNotFoundException("LessonId " + id + "not found"));

			    lesson.setDescription(lessonRequest.getDescription());
			    lesson.setLessonname(lessonRequest.getLessonname());

			    Lesson lessonUpdated = lessonService.updateLesson(id, lesson);
				
				ResponseEntity<Lesson> responseEntity = new ResponseEntity<Lesson>(lessonUpdated, HttpStatus.OK);

				return responseEntity;
			    
	}

	@PostMapping("/instructors/{instructorId}/lessons")
	public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lessonRequest, @PathVariable(value = "instructorId") long instructorId){
		
		lessonRequest.setLessonname(lessonRequest.getLessonname());
		lessonRequest.setDescription(lessonRequest.getDescription());

		Lesson lesson = lessonService.createLesson(lessonRequest, instructorId);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lesson.getId())
				.toUri(); 

		return ResponseEntity.created(uri).build();	}

}