package com.sideproject1.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "students")
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String studentname;

	private String contact;

	@ManyToMany(fetch = FetchType.LAZY,
		      cascade = {
		          CascadeType.PERSIST,
		          CascadeType.MERGE
		      },
		      mappedBy = "students")
		  @JsonIgnore
		  private Set<Lesson> lessons = new HashSet<>();	
	
	public Student() {

	}

	public Student(long id, String studentname, String contact) {
		super();
		this.id = id;
		this.studentname = studentname;
		this.contact = contact;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStudentname() {
		return studentname;
	}

	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}

	
	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Set<Lesson> getLessons() {
	    return lessons;
	  }

	  public void setLessons(Set<Lesson> lessons) {
	    this.lessons = lessons;
	  }
}
