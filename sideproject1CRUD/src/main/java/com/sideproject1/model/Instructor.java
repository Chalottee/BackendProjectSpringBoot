package com.sideproject1.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Instructor {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Instructor_generator")
	private Long id;
	private String instructorname;	
	private String address;

	

	public Instructor() {

	}

	public Instructor(long id, String instructorname,  String address) {
		super();
		this.id = id;
		this.instructorname = instructorname;
		
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getInstructorname() {
		return instructorname;
	}

	public void setInstructorname(String instructorname) {
		this.instructorname = instructorname;
	}


	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
}
