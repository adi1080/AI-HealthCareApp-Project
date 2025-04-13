package com.MajorProject.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.MajorProject.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
}
