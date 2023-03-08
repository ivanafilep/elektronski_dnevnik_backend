package com.ivana.tema8.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class Polugodiste {
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column
	@NotNull(message = "Polugodiste mora biti navedeno.")
	@Max (value = 2, message = "Polugodiste ne moze biti vece od 2.")
	private Integer brojPolugodista;
	
	@JsonManagedReference
	@JsonIgnore
	@OneToMany(mappedBy = "polugodiste", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Ocena> ocene = new ArrayList<Ocena>();

	public Polugodiste() {
		super();
	}

	public Polugodiste(Integer brojPolugodista, List<Ocena> ocene) {
		super();
		this.brojPolugodista = brojPolugodista;
		this.ocene = ocene;
	}

	public Integer getBrojPolugodista() {
		return brojPolugodista;
	}

	public void setBrojPolugodista(Integer brojPolugodista) {
		this.brojPolugodista = brojPolugodista;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Ocena> getOcene() {
		return ocene;
	}

	public void setOcene(List<Ocena> ocene) {
		this.ocene = ocene;
	}
	
	
	
	

}
