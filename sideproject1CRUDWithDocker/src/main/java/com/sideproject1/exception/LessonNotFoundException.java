package com.sideproject1.exception;

public class LessonNotFoundException extends RuntimeException {
  
	private static final long serialVersionUID = 179856888745840942L;

	public LessonNotFoundException(Long id) {
        super("Lesson id not found : " + id);
    }
	
	public LessonNotFoundException(String message, Long id) {
        super("Lesson id not found : " + id + " TODO " + message);
    }
}
