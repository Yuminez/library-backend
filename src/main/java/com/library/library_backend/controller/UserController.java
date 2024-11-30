package com.library.library_backend.controller;

import com.library.library_backend.model.User;
import com.library.library_backend.repository.UserRepository;
import com.library.libray_backend.request.LoginRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	private List<Long> admins = List.of(16L,17L,23L);

	// Crea un nuovo utente
	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody User user) {
		try {
			User savedUser = userRepository.save(user);
			return ResponseEntity.ok(savedUser);
		} catch (DataIntegrityViolationException e) {
			// Rileva l'errore di duplicazione dell'email e invia un messaggio
			// personalizzato
			return ResponseEntity.status(HttpStatus.CONFLICT).body("L'Account è già in esistente.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Errore durante la creazione dell'utente");
		}
	}

	// Login
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
		try {
			// Cerca l'utente per email e password
			Optional<User> user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword());

			if (user.isPresent()) {
				if (admins.contains(user.get().getId())) {
					ResponseCookie adminsCookie = ResponseCookie.from("isAdmin", user.get().getId().toString())
							.httpOnly(true).secure(true).path("/").maxAge(1200).sameSite("Strict").build();
					return ResponseEntity.ok().header("Set-Cookie", adminsCookie.toString()).body("Login da Admin!");
				} else {
					// Crea un cookie con l'ID dell'utente
					ResponseCookie cookie = ResponseCookie.from("userId", user.get().getId().toString()).httpOnly(true)
							.secure(true) // Usa `true` solo su connessioni HTTPS in produzione
							.path("/").maxAge(1200) // Durata del cookie (20 minuti)
							.sameSite("Strict") // Imposta la policy SameSite per il cookie
							.build();
					return ResponseEntity.ok().header("Set-Cookie", cookie.toString())
							.body("Login effettuato con successo!");
				}
			} else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenziali non valide");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il login");
		}
	}

	// Recupera tutti gli utenti
	@GetMapping("/all")
	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	// Recupera un utente per ID
	@GetMapping("/{id}")
	public User getUserById(@PathVariable Long id) {
		return userRepository.findById(id).orElse(null);
	}
}
