package com.example.complaint_tracker.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.complaint_tracker.model.StaffDetails;

public interface StaffDetailsRepo extends JpaRepository<StaffDetails,Long> {

	public StaffDetails findByEmail(String email);

	public StaffDetails findByStaffid(String id);
}
