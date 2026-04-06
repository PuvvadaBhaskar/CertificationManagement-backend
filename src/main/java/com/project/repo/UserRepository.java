package com.project.repo;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}