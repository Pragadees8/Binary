package com.binary.engine.controller;

import com.binary.engine.dto.ApiResponse;
import com.binary.engine.service.AdminService;
import com.binary.engine.service.KycService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
// neww
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private KycService kycService;
	
	@PostMapping("/approve-user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> approveUser(@PathVariable Long userId) {
		ApiResponse response = adminService.approveUser(userId);
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@PostMapping("/reject-user/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> rejectUser(@PathVariable Long userId, @RequestParam String reason) {
		ApiResponse response = adminService.rejectUser(userId, reason);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@PostMapping("/approve-kyc/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> approveKyc(@PathVariable Long userId) {
		ApiResponse response = kycService.approveKyc(userId);
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@PostMapping("/reject-kyc/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> rejectKyc(@PathVariable Long userId, @RequestParam String reason) {
		ApiResponse response = kycService.rejectKyc(userId, reason);
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/all-users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> getAllUsers() {
		ApiResponse response = adminService.getAllUsers();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/pending-users")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> getPendingUsers() {
		ApiResponse response = adminService.getPendingUsers();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	@GetMapping("/pending-kyc")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ApiResponse> getPendingKyc() {
		ApiResponse response = kycService.getPendingKyc();
		
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
