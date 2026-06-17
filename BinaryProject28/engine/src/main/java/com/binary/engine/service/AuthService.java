package com.binary.engine.service;

import com.binary.engine.dto.LoginRequest;
import com.binary.engine.dto.LoginResponse;
import com.binary.engine.dto.RegisterRequest;
import com.binary.engine.dto.ApiResponse;

public interface AuthService {
	
	ApiResponse registerUser(RegisterRequest request);
	
	LoginResponse loginUser(LoginRequest request);
	
	ApiResponse sendOtp(Long userId);
	
	ApiResponse verifyOtp(Long userId, String otp);
}
