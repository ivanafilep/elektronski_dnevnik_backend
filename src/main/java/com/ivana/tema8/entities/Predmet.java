package com.ivana.tema8.entities;

import java.util.ArrayList;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class Predmet {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column
	@NotNull(message = "Naziv predmeta mora biti naveden.")
	@Size(min = 2, max = 20, message = "Naziv predmeta mora biti izmedju {min} and {max} karaktera.")
	private String nazivPredmeta;
	@Column
	@NotNull(message = "Nedeljni fond casova mora biti naveden.")
	@Max (value = 50, message = "Nedeljni fond casova ne moze biti veci od 50 casova")
	private Integer nedeljniFondCasova;
	

	
	@JsonIgnore
	@OneToMany(mappedBy = "predmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<NastavnikPredmet> nastavnikPredmet = new ArrayList<NastavnikPredmet>();
	
	public Predmet() {
		super();
	}

	public Predmet(Integer id, String nazivPredmeta, Integer nedeljniFondCasova, List<NastavnikPredmet> nastavnikPredmet) {
		super();
		this.id = id;
		this.nazivPredmeta = nazivPredmeta;
		this.nedeljniFondCasova = nedeljniFondCasova;
		this.nastavnikPredmet = nastavnikPredmet;
		
		
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNazivPredmeta() {
		return nazivPredmeta;
	}

	public void setNazivPredmeta(String nazivPredmeta) {
		this.nazivPredmeta = nazivPredmeta;
	}

	public Integer getNedeljniFondCasova() {
		return nedeljniFondCasova;
	}

	public void setNedeljniFondCasova(Integer nedeljniFondCasova) {
		this.nedeljniFondCasova = nedeljniFondCasova;
	}

	public List<NastavnikPredmet> getNastavnikPredmet() {
		return nastavnikPredmet;
	}

	public void setNastavnikPredmet(List<NastavnikPredmet> nastavnikPredmet) {
		this.nastavnikPredmet = nastavnikPredmet;
	}

	

	
	 
	
	
	
	

}
