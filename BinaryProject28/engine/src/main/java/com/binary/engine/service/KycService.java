package com.binary.engine.service;

import com.binary.engine.dto.KycRequest;
import com.binary.engine.dto.ApiResponse;

public interface KycService {
	
	ApiResponse submitKyc(KycRequest request);
	
	ApiResponse getKycDetails(Long userId);
	
	ApiResponse approveKyc(Long userId);
	
	ApiResponse rejectKyc(Long userId, String reason);
	
	ApiResponse getPendingKyc();
}
