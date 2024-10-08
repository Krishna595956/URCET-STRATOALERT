package com.example.complaint_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.complaint_tracker.model.ComplaintDetails;

public interface ComplaintsRepo extends JpaRepository<ComplaintDetails, Long> {
	 
}
