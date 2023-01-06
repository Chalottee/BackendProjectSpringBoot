package com.sideproject1.exception;

public class InstructorNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 179856888745840942L;

	public InstructorNotFoundException(Long id) {
        super("Instructor id not found : " + id);
    }
	
	public InstructorNotFoundException(String message, Long id) {
        super("Instructor id not found : " + id + " TODO " + message);
    }
}
