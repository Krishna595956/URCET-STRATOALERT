package com.example.complaint_tracker.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "complaints")
public class ComplaintDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String description;

    @Column
    private String evidenceFileName; // Store the filename of the uploaded evidence

    @Column(nullable = false)
    private String desiredResolution;

    @Column(nullable = false)
    private LocalDate dateOfIncident;
      
    @Column
    private String givenBy;
    
    @Column
    private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGivenBy() {
		return givenBy;
	}

	public void setGivenBy(String givenBy) {
		this.givenBy = givenBy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDesiredResolution() {
		return desiredResolution;
	}

	public void setDesiredResolution(String desiredResolution) {
		this.desiredResolution = desiredResolution;
	}

	public LocalDate getDateOfIncident() {
		return dateOfIncident;
	}

	public void setDateOfIncident(LocalDate dateOfIncident) {
		this.dateOfIncident = dateOfIncident;
	}

	public String getEvidenceFileName() {
		return evidenceFileName;
	}

	public void setEvidenceFileName(String fileName) {
		this.evidenceFileName = fileName;
	}

    
}