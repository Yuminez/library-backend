package com.library.library_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

@Entity
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titolo;
    private String autore;
    private String isbn;
    private int disponibile;
    
    // Nuovo campo per lo stato del libro
    @Enumerated(EnumType.STRING)
    private StatoLibro stato;

    // Getter e Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public StatoLibro getStato() {
        return stato;
    }

    public void setStato(StatoLibro stato) {
        this.stato = stato;
    }

    public enum StatoLibro {
        PRESTATO,
        IN_STOCK,
        NON_IN_STOCK
    }
}

