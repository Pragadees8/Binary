package com.binary.engine.service.impl;

import com.binary.engine.dto.LoginRequest;
import com.binary.engine.dto.LoginResponse;
import com.binary.engine.dto.RegisterRequest;
import com.binary.engine.dto.ApiResponse;
import com.binary.engine.entity.User;
import com.binary.engine.entity.OtpVerification;
import com.binary.engine.repository.UserRepository;
import com.binary.engine.repository.OtpVerificationRepository;
import com.binary.engine.security.JwtTokenProvider;
import com.binary.engine.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OtpVerificationRepository otpRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Override
	public ApiResponse registerUser(RegisterRequest request) {
		
		if (userRepository.existsByEmail(request.getEmail())) {
			return new ApiResponse("Email already registered", false);
		}
		
		if (userRepository.existsByMobile(request.getMobile())) {
			return new ApiResponse("Mobile number already registered", false);
		}
		
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setMobile(request.getMobile());
		user.setRole(User.UserRole.USER);
		user.setStatus(User.UserStatus.PENDING);
		user.setOtpVerified(false);
		user.setKycVerified(false);
		user.setCreatedAt(System.currentTimeMillis());
		user.setUpdatedAt(System.currentTimeMillis());
		
		User savedUser = userRepository.save(user);
		
		return new ApiResponse("User registered successfully. Please verify OTP.", true, savedUser.getId());
	}
	
	@Override
	public LoginResponse loginUser(LoginRequest request) {
		
		var userOpt = userRepository.findByEmail(request.getEmail());
		
		if (userOpt.isEmpty()) {
			return new LoginResponse(null, "Invalid email or password", null, null, null);
		}
		
		User user = userOpt.get();
		
		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			return new LoginResponse(null, "Invalid email or password", null, null, null);
		}
		
		if (!user.getStatus().equals(User.UserStatus.ACTIVE)) {
			return new LoginResponse(null, "User account is not active. Status: " + user.getStatus(), null, null, null);
		}
		
		String token = jwtTokenProvider.generateToken(user.getEmail(), user.getRole().toString());
		
		return new LoginResponse(token, "Login successful", user.getId(), user.getEmail(), user.getRole().toString());
	}
	
	@Override
	public ApiResponse sendOtp(Long userId) {
		
		var userOpt = userRepository.findById(userId);
		
		if (userOpt.isEmpty()) {
			return new ApiResponse("User not found", false);
		}
		
		User user = userOpt.get();
		
		String otp = generateOtp();
		long expiryTime = System.currentTimeMillis() + (10 * 60 * 1000); // 10 minutes
		
		OtpVerification otpVerification = new OtpVerification();
		otpVerification.setUserId(userId);
		otpVerification.setOtp(otp);
		otpVerification.setExpiryTime(expiryTime);
		otpVerification.setIsVerified(false);
		otpVerification.setEmail(user.getEmail());
		otpVerification.setCreatedAt(System.currentTimeMillis());
		
		otpRepository.save(otpVerification);
		
		// In a real application, you would send OTP via email or SMS
		System.out.println("OTP for " + user.getEmail() + " is: " + otp);
		
		return new ApiResponse("OTP sent successfully to " + user.getEmail() + ". OTP: " + otp, true);
	}
	
	@Override
	public ApiResponse verifyOtp(Long userId, String otp) {
		
		var otpOpt = otpRepository.findTopByUserIdOrderByCreatedAtDesc(userId);
		
		if (otpOpt.isEmpty()) {
			return new ApiResponse("OTP not found. Please send OTP first.", false);
		}
		
		OtpVerification otpVerification = otpOpt.get();
		
		if (System.currentTimeMillis() > otpVerification.getExpiryTime()) {
			return new ApiResponse("OTP has expired", false);
		}
		
		if (!otpVerification.getOtp().equals(otp)) {
			return new ApiResponse("Invalid OTP", false);
		}
		
		otpVerification.setIsVerified(true);
		otpRepository.save(otpVerification);
		
		var userOpt = userRepository.findById(userId);
		if (userOpt.isPresent()) {
			User user = userOpt.get();
			user.setOtpVerified(true);
			user.setStatus(User.UserStatus.OTP_VERIFIED);
			user.setUpdatedAt(System.currentTimeMillis());
			userRepository.save(user);
		}
		
		return new ApiResponse("OTP verified successfully", true);
	}
	
	private String generateOtp() {
		Random random = new Random();
		int otp = 100000 + random.nextInt(900000);
		return String.valueOf(otp);
	}
}
