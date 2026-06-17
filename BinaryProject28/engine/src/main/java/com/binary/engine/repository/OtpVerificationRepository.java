package com.binary.engine.repository;

import com.binary.engine.entity.OtpVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpVerificationRepository extends JpaRepository<OtpVerification, Long> {
	
	Optional<OtpVerification> findByUserIdAndEmail(Long userId, String email);
	
	Optional<OtpVerification> findTopByUserIdOrderByCreatedAtDesc(Long userId);
}
