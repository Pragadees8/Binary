package com.binary.engine.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "kyc_details")
public class KycDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private Long userId;
	
	@Column(nullable = false, unique = true)
	private String panNumber;
	
	@Column(nullable = true)
	private String aadharNumber;
	
	@Column(nullable = true)
	private String firstName;
	
	@Column(nullable = true)
	private String lastName;
	
	@Column(nullable = true)
	private String dateOfBirth;
	
	@Column(nullable = true)
	private String address;
	
	@Column(nullable = true)
	private String city;
	
	@Column(nullable = true)
	private String state;
	
	@Column(nullable = true)
	private String pincode;
	
	@Column(nullable = true)
	@Enumerated(EnumType.STRING)
	private KycStatus kycStatus;
	
	@Column(nullable = true)
	private String rejectionReason;
	
	@Column(nullable = true)
	private Long submittedAt;
	
	@Column(nullable = true)
	private Long approvedAt;
	
	public enum KycStatus {
		PENDING, APPROVED, REJECTED
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getPanNumber() {
		return panNumber;
	}

	public void setPanNumber(String panNumber) {
		this.panNumber = panNumber;
	}

	public String getAadharNumber() {
		return aadharNumber;
	}

	public void setAadharNumber(String aadharNumber) {
		this.aadharNumber = aadharNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getPincode() {
		return pincode;
	}

	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

	public KycStatus getKycStatus() {
		return kycStatus;
	}

	public void setKycStatus(KycStatus kycStatus) {
		this.kycStatus = kycStatus;
	}

	public String getRejectionReason() {
		return rejectionReason;
	}

	public void setRejectionReason(String rejectionReason) {
		this.rejectionReason = rejectionReason;
	}

	public Long getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(Long submittedAt) {
		this.submittedAt = submittedAt;
	}

	public Long getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Long approvedAt) {
		this.approvedAt = approvedAt;
	}
}
