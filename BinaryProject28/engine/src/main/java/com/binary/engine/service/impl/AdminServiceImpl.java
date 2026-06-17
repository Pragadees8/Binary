package com.binary.engine.service.impl;

import com.binary.engine.dto.ApiResponse;
import com.binary.engine.entity.User;
import com.binary.engine.repository.UserRepository;
import com.binary.engine.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public ApiResponse approveUser(Long userId) {
		
		var userOpt = userRepository.findById(userId);
		
		if (userOpt.isEmpty()) {
			return new ApiResponse("User not found", false);
		}
		
		User user = userOpt.get();
		
		if (!user.getKycVerified()) {
			return new ApiResponse("User KYC is not verified yet", false);
		}
		
		user.setStatus(User.UserStatus.ACTIVE);
		user.setUpdatedAt(System.currentTimeMillis());
		
		userRepository.save(user);
		
		return new ApiResponse("User approved successfully and activated", true);
	}
	
	@Override
	public ApiResponse rejectUser(Long userId, String reason) {
		
		var userOpt = userRepository.findById(userId);
		
		if (userOpt.isEmpty()) {
			return new ApiResponse("User not found", false);
		}
		
		User user = userOpt.get();
		user.setStatus(User.UserStatus.REJECTED);
		user.setUpdatedAt(System.currentTimeMillis());
		
		userRepository.save(user);
		
		return new ApiResponse("User rejected. Reason: " + reason, false);
	}
	
	@Override
	public ApiResponse getAllUsers() {
		
		List<User> users = userRepository.findAll();
		
		return new ApiResponse("All users retrieved successfully", true, users);
	}
	
	@Override
	public ApiResponse getPendingUsers() {
		
		List<User> allUsers = userRepository.findAll();
		
		List<User> pendingUsers = allUsers.stream()
			.filter(user -> user.getStatus() == User.UserStatus.KYC_SUBMITTED || 
						   user.getStatus() == User.UserStatus.OTP_VERIFIED)
			.collect(Collectors.toList());
		
		return new ApiResponse("Pending users retrieved successfully", true, pendingUsers);
	}
}
