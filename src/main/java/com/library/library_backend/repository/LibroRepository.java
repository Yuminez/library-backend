package com.library.library_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.library.library_backend.model.Libro;
import com.library.library_backend.model.Libro.StatoLibro;

import jakarta.transaction.Transactional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
	
    @Modifying
    @Transactional
    @Query("UPDATE Libro l SET l.stato = :stato WHERE l.id = :id")
    void updateStato(Long id, StatoLibro stato);
}