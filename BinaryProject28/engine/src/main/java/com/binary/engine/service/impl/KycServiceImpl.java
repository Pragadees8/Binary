package com.binary.engine.service.impl;

import com.binary.engine.dto.KycRequest;
import com.binary.engine.dto.ApiResponse;
import com.binary.engine.entity.KycDetails;
import com.binary.engine.entity.User;
import com.binary.engine.repository.KycDetailsRepository;
import com.binary.engine.repository.UserRepository;
import com.binary.engine.service.KycService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KycServiceImpl implements KycService {
	
	@Autowired
	private KycDetailsRepository kycRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ApiResponse submitKyc(KycRequest request) {
		
		var userOpt = userRepository.findById(request.getUserId());
		if (userOpt.isEmpty()) {
			return new ApiResponse("User not found", false);
		}
		
		User user = userOpt.get();
		if (!user.getOtpVerified()) {
			return new ApiResponse("Please verify OTP first", false);
		}
		
		// Check if PAN already has 3 registrations
		long panCount = kycRepository.countByPanNumber(request.getPanNumber());
		if (panCount >= 3) {
			return new ApiResponse("PAN number has reached maximum registration limit (3)", false);
		}
		
		var existingKyc = kycRepository.findByUserId(request.getUserId());
		if (existingKyc.isPresent()) {
			return new ApiResponse("KYC already submitted for this user", false);
		}
		
		KycDetails kyc = new KycDetails();
		kyc.setUserId(request.getUserId());
		kyc.setPanNumber(request.getPanNumber());
		kyc.setAadharNumber(request.getAadharNumber());
		kyc.setFirstName(request.getFirstName());
		kyc.setLastName(request.getLastName());
		kyc.setDateOfBirth(request.getDateOfBirth());
		kyc.setAddress(request.getAddress());
		kyc.setCity(request.getCity());
		kyc.setState(request.getState());
		kyc.setPincode(request.getPincode());
		kyc.setKycStatus(KycDetails.KycStatus.PENDING);
		kyc.setSubmittedAt(System.currentTimeMillis());
		
		kycRepository.save(kyc);
		
		user.setStatus(User.UserStatus.KYC_SUBMITTED);
		user.setUpdatedAt(System.currentTimeMillis());
		userRepository.save(user);
		
		return new ApiResponse("KYC submitted successfully. Awaiting admin approval.", true);
	}
	
	@Override
	public ApiResponse getKycDetails(Long userId) {
		
		var kycOpt = kycRepository.findByUserId(userId);
		
		if (kycOpt.isEmpty()) {
			return new ApiResponse("KYC details not found", false);
		}
		
		return new ApiResponse("KYC details retrieved successfully", true, kycOpt.get());
	}
	
	@Override
	public ApiResponse approveKyc(Long userId) {
		
		var kycOpt = kycRepository.findByUserId(userId);
		
		if (kycOpt.isEmpty()) {
			return new ApiResponse("KYC details not found", false);
		}
		
		KycDetails kyc = kycOpt.get();
		kyc.setKycStatus(KycDetails.KycStatus.APPROVED);
		kyc.setApprovedAt(System.currentTimeMillis());
		
		kycRepository.save(kyc);
		
		var userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setKycVerified(true);
			user.setStatus(User.UserStatus.APPROVED);
			user.setUpdatedAt(System.currentTimeMillis());
			userRepository.save(user);
		}
		
		return new ApiResponse("KYC approved successfully", true);
	}
	
	@Override
	public ApiResponse rejectKyc(Long userId, String reason) {
		
		var kycOpt = kycRepository.findByUserId(userId);
		
		if (kycOpt.isEmpty()) {
			return new ApiResponse("KYC details not found", false);
		}
		
		KycDetails kyc = kycOpt.get();
		kyc.setKycStatus(KycDetails.KycStatus.REJECTED);
		kyc.setRejectionReason(reason);
		
		kycRepository.save(kyc);
		
		var userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setStatus(User.UserStatus.REJECTED);
			user.setUpdatedAt(System.currentTimeMillis());
			userRepository.save(user);
		}
		
		return new ApiResponse("KYC rejected: " + reason, false);
	}
	
	@Override
	public ApiResponse getPendingKyc() {
		
		List<KycDetails> pendingKyc = kycRepository.findAll().stream()
			.filter(kyc -> kyc.getKycStatus() == KycDetails.KycStatus.PENDING)
			.toList();
		
		return new ApiResponse("Pending KYC details retrieved", true, pendingKyc);
	}
}
