package com.binary.engine.repository;

import com.binary.engine.entity.KycDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface KycDetailsRepository extends JpaRepository<KycDetails, Long> {
	
	Optional<KycDetails> findByUserId(Long userId);
	
	Optional<KycDetails> findByPanNumber(String panNumber);
	
	List<KycDetails> findByPanNumberAndKycStatus(String panNumber, KycDetails.KycStatus status);
	
	long countByPanNumber(String panNumber);
}
