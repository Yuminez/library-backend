package com.library.library_backend.controller;

import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.library.library_backend.model.Libro.StatoLibro;
import com.library.library_backend.model.Prestito;
import com.library.library_backend.repository.LibroRepository;
import com.library.library_backend.repository.PrestitoRepository;

import jakarta.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/prestiti")
public class PrestitoController {

	@Autowired
	private PrestitoRepository prestitoRepository;

	@Autowired
	private LibroRepository libroRepository;

	// Restituisce tutti i prestiti
	@GetMapping
	public List<Prestito> getAllPrestiti() {
		return prestitoRepository.findAll();
	}

	// Restituisce un prestito specifico per ID
	@GetMapping("/{id}")
	public Prestito getPrestitoById(@PathVariable Long id) {
		return prestitoRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Prestito non trovato con id " + id));
	}

	@GetMapping("/user/{userId}")
	public List<Prestito> getPrestitoByUserIdId(@PathVariable Long userId) {
		return prestitoRepository.findAllByIdUtente(userId);
	}

	// Crea un nuovo prestito
	@PostMapping
	public ResponseEntity<Prestito> creaPrestito(@RequestBody Prestito prestito) {
		if (prestitoRepository.findByDataRestituzioneAndIdLibro(null, prestito.getIdLibro()).isPresent()) {
			return new ResponseEntity<Prestito>(HttpStatus.BAD_REQUEST);
		}
		libroRepository.updateStato(prestito.getIdLibro(), StatoLibro.PRESTATO);

		return ResponseEntity.ok(prestitoRepository.save(prestito));
	}

	// Aggiorna un prestito esistente
	@PutMapping("/{id}")
	public ResponseEntity<Prestito> updatePrestito(@PathVariable Long id, @RequestBody Prestito prestitoDetails) {
		try {
			Prestito prestito = prestitoRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Prestito non trovato con id " + id));

			prestitoDetails.setId(id);
			if (prestitoDetails.getDataPrestito() == null) {
				prestitoDetails.setDataPrestito(prestito.getDataPrestito());
			}

			return ResponseEntity.ok(prestitoRepository.save(prestitoDetails));
		} catch (Exception exception) {
			return new ResponseEntity<Prestito>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/flush")
	public void flush() {
		prestitoRepository.deleteAll();
	}

	// Elimina un prestito
	@DeleteMapping("/{id}")
	public void deletePrestito(@PathVariable Long id) {
		prestitoRepository.deleteById(id);
	}

	@PutMapping("restituzione/{id}")
	public void restituzionePrestito(@PathVariable Long id) {
		try {
			Prestito prestito = prestitoRepository.findById(id)
					.orElseThrow(() -> new RuntimeException("Prestito non trovato con id " + id));

			prestitoRepository.updateDataRestituzione(id, Date.valueOf(LocalDate.now()));

			libroRepository.updateStato(prestito.getIdLibro(), StatoLibro.IN_STOCK);
		} catch (Exception e) {

		}
	}

	// Restituisce tutti i prestiti in ritardo
	@GetMapping("/ritardo")
	public List<Prestito> getPrestitiInRitardo() {
		LocalDate today = LocalDate.now();
		return prestitoRepository.findPrestitiInRitardo(today.minusDays(7L));
	}
}
