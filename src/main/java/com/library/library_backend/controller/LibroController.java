package com.library.library_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.library.library_backend.model.Libro;
import com.library.library_backend.model.Libro.StatoLibro;
import com.library.library_backend.repository.LibroRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/libri")
public class LibroController {

    @Autowired
    private LibroRepository libroRepository;

    // Visualizzare tutti i libri (accesso per USER e ADMIN)
    @GetMapping
    public List<Libro> getAllLibri() {
        return libroRepository.findAll();
    }

    // Visualizzare un libro per ID (accesso per USER e ADMIN)
    @GetMapping("/{id}")
    public Optional<Libro> getLibroById(@PathVariable Long id) {
        return libroRepository.findById(id);
    }

    // Creare un nuovo libro (solo ADMIN)
    @PostMapping("/admin")
    public Libro creaLibro(@RequestBody Libro libro) {
        return libroRepository.save(libro);
    }
    
    @PostMapping("/admin/saveAll")
    public void creaTuttiLibri(@RequestBody List<Libro> libri) {
         libroRepository.saveAll(libri);
    }

    // Aggiornare un libro esistente (solo ADMIN)
    @PutMapping("/admin/{id}")
    public Libro updateLibro(@PathVariable Long id, @RequestBody Libro libroDetails) {
        Libro libro = libroRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Libro non trovato con id " + id));
        libroDetails.setId(id);
        return libroRepository.save(libroDetails);
    }

    // Cancellare un libro (solo ADMIN)
    @DeleteMapping("/admin/{id}")
    public void deleteLibro(@PathVariable Long id) {
        libroRepository.deleteById(id);
    }
}
