package com.sideproject1.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.sideproject1.model.Instructor;

@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long>, JpaSpecificationExecutor<Instructor> {

	List<Instructor> findByInstructorname(String instructorname);
}
