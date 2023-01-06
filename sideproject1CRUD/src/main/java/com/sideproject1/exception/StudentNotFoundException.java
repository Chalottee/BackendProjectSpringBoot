package com.sideproject1.exception;

public class StudentNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 179856888745840942L;

	public StudentNotFoundException(Long id) {
        super("Student id not found : " + id);
    }
	
	public StudentNotFoundException(String message, Long id) {
        super("Student id not found : " + id + " TODO " + message);
    }
}
