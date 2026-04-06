package com.project.repo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.project.model.Certification;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CertificationRepository 
       extends JpaRepository<Certification, Long> {

	List<Certification> findByUserId(Long userId);
	long countByUserId(Long userId);

	long countByUserIdAndExpiryDateBefore(Long userId, LocalDate date);

	long countByUserIdAndExpiryDateAfter(Long userId, LocalDate date);

	long countByUserIdAndExpiryDateBetween(Long userId, LocalDate start, LocalDate end);
	Page<Certification> findByUserId(Long userId, Pageable pageable);

	Page<Certification> findByUserIdAndTitleContainingIgnoreCase(
	        Long userId, String title, Pageable pageable);

	Page<Certification> findByUserIdAndStatus(
	        Long userId, String status, Pageable pageable);
	void deleteByUserId(Long userId);
}