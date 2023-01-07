package com.sideproject1.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Lesson {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Lesson_generator")
	private Long id;
	
	private String description;
	private String lessonname;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "instructor_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonIgnore
	private Instructor instructor;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(name = "lesson_students", 
			joinColumns = { @JoinColumn(name = "lesson_id")},
			inverseJoinColumns = {@JoinColumn(name = "student_id")})
	private Set<Student> students = new HashSet<>();
	

	public Lesson() {

	}

	public Lesson(long id, String description, String lessonname) {
		super();
		this.id = id;
		this.description = description;
		this.lessonname = lessonname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}



	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getLessonname() {
		return lessonname;
	}

	public void setLessonname(String lessonname) {
		this.lessonname = lessonname;
	}
	
	public Instructor getInstructor() {
		return instructor;
	}

	public void setInstructor(Instructor instructor) {
		this.instructor = instructor;
	}

	public Set<Student> getStudent() {
		return students;
	}

	public void setStudent(Set<Student> students) {
 	this.students = students;
	}
	
	public void addStudent(Student student) {
	    this.students.add(student);
	    student.getLessons().add(this);
	  }
	  
	  public void removeStudent(long studentId) {
	    Student student = this.students.stream().filter(t -> t.getId() == studentId).findFirst().orElse(null);
	    if (student != null) {
	      this.students.remove(student);
	      student.getLessons().remove(this);
	    }
	  }
}