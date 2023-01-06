package com.sideproject1.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.sideproject1.model.Instructor;
import com.sideproject1.service.InstructorService;

@RestController
public class InstructorController {

	@Autowired
	private InstructorService instructorService; 
	
	@GetMapping("/instructors")
    public List<Instructor> getAllInstructors(){
		List<Instructor> instructors = instructorService.getAllInstructors();
		
		return instructors;
	}
	
	@GetMapping("/instructors/{id}")
	public Instructor getInstructorById(@PathVariable Long id) {
		Instructor instructor = instructorService.getInstructorById(id);
		
		return instructor;
	}
	
	@PostMapping("/instructors/{id}")
	public ResponseEntity<Void> createInstructor(@RequestBody Instructor instructor,@PathVariable Long id) {
		Instructor createdInstructor = instructorService.createInstructor(instructor,id);
		
		if(createdInstructor == null) 
			return ResponseEntity.noContent().build();
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(createdInstructor.getId())
				.toUri(); 

		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping("/instructors/{id}")
	public ResponseEntity<Instructor> updateInstructor(@PathVariable Long id,@RequestBody Instructor instructor){
		Instructor updatedInstructor = instructorService.updateInstructor(id, instructor);
		
		ResponseEntity<Instructor> responseEntity = new ResponseEntity<Instructor>(updatedInstructor, HttpStatus.OK);

		return responseEntity;
	}
	
	@DeleteMapping("/instructors/{id}")
	public ResponseEntity<Void> deleteInstructor(@PathVariable Long id){
		instructorService.deleteInstructor(id);
		
		ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
		return responseEntity;
	}
	
	@DeleteMapping("/instructors")
	public ResponseEntity<Void> deleteAllInstructors(){
		instructorService.deleteAllInstructors();
		
		ResponseEntity<Void> responseEntity = ResponseEntity.noContent().build();
		return responseEntity;
	}
	

}
