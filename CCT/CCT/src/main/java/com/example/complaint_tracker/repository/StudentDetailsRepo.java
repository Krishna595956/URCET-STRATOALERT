package com.example.complaint_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.complaint_tracker.model.StudentDetails;

public interface StudentDetailsRepo extends JpaRepository<StudentDetails,Long>{
	public StudentDetails findByEmail(String mail);
	public StudentDetails findByRollNo(String no);
}
