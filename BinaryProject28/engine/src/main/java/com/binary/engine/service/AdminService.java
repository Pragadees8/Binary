package com.binary.engine.service;

import com.binary.engine.dto.ApiResponse;

public interface AdminService {
	
	ApiResponse approveUser(Long userId);
	
	ApiResponse rejectUser(Long userId, String reason);
	
	ApiResponse getAllUsers();
	
	ApiResponse getPendingUsers();
}
