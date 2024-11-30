package com.library.library_backend.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;

@Entity
public class Prestito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private Long idLibro;

    private Long idUtente;

    private Date dataPrestito;
    private Date dataRestituzione;
    
    // Getters e Setters
    
	
	@PrePersist
	private void onCreate() {
		this.dataPrestito= Date.valueOf(LocalDate.now());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(Long idLibro) {
		this.idLibro = idLibro;
	}

	public Long getIdUtente() {
		return idUtente;
	}

	public void setIdUtente(Long idUtente) {
		this.idUtente = idUtente;
	}

	public Date getDataPrestito() {
		return dataPrestito;
	}

	public void setDataPrestito(Date dataPrestito) {
		this.dataPrestito = dataPrestito;
	}

	public Date getDataRestituzione() {
		return dataRestituzione;
	}

	public void setDataRestituzione(Date dataRestituzione) {
		this.dataRestituzione = dataRestituzione;
	}
}
