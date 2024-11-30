package com.library.library_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.library.library_backend.model.Prestito;
import com.library.library_backend.model.Libro.StatoLibro;

import jakarta.transaction.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PrestitoRepository extends JpaRepository<Prestito, Long> {
    // Query per ottenere tutti i prestiti che sono in ritardo
	// Recupera l'entità dal database
    @Query("SELECT p FROM Prestito p WHERE p.dataRestituzione IS NULL AND p.dataPrestito < :data")
    public List<Prestito> findPrestitiInRitardo(LocalDate data);

	public List<Prestito> findAllByIdUtente(Long userId);
	
	@Modifying
    @Transactional
    @Query("UPDATE Prestito p SET p.dataRestituzione = :date WHERE p.id = :id")
    public void updateDataRestituzione(Long id, Date date);

	public Optional<Prestito> findByDataRestituzioneAndIdLibro(Date date, Long idLibro);
}