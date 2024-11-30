package com.library.library_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.library.library_backend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Puoi aggiungere metodi di query personalizzati se necessario, ad esempio:
    // Optional<User> findByUsername(String username);
	 Optional<User> findByEmailAndPassword(String email, String password);
}
