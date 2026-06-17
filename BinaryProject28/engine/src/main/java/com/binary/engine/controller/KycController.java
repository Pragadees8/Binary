package com.binary.engine.controller;

import com.binary.engine.dto.KycRequest;
import com.binary.engine.dto.ApiResponse;
import com.binary.engine.service.KycService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kyc")
@CrossOrigin(origins = "*", maxAge = 3600)
public class KycController {
	
	@Autowired
	private KycService kycService;
	
	@PostMapping("/submit")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> submitKyc(@RequestBody KycRequest request) {
		ApiResponse response = kycService.submitKyc(request);
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
	}
	
	@GetMapping("/{userId}")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ApiResponse> getKycDetails(@PathVariable Long userId) {
		ApiResponse response = kycService.getKycDetails(userId);
		
		if (response.isSuccess()) {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
		}
	}
}
