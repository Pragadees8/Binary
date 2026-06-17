package com.binary.engine.controller;

import com.binary.engine.dto.LoginRequest;
import com.binary.engine.dto.LoginResponse;
import com.binary.engine.dto.RegisterRequest;
import com.binary.engine.dto.ApiResponse;
import com.binary.engine.dto.OtpRequest;
import com.binary.engine.service.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest request) {
		ApiResponse response = authService.registerUser(request);
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginRequest request) {
		LoginResponse response = authService.loginUser(request);
		
		if (response.getToken() != null) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
		}
	}
	
	@PostMapping("/send-otp/{userId}")
	public ResponseEntity<ApiResponse> sendOtp(@PathVariable Long userId) {
		ApiResponse response = authService.sendOtp(userId);
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@PostMapping("/verify-otp")
	public ResponseEntity<ApiResponse> verifyOtp(@RequestBody OtpRequest request) {
		ApiResponse response = authService.verifyOtp(request.getUserId(), request.getOtp());
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
}
