package com.ivana.tema8.entities;

import java.util.ArrayList;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class Ucenik extends Korisnik {
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "ucenik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Ocena> ocene = new ArrayList<Ocena>();
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "roditelj")
	private Roditelj roditelj;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "Ucenik_NastavnikPredmet", joinColumns = 
		{@JoinColumn(name = "ucenik_id", nullable = false, updatable = false)},
		inverseJoinColumns = {@JoinColumn(name = "NastavnikPredmet_id",
			nullable = false, updatable = false) })
	protected Set<NastavnikPredmet> nastavnikPredmet = new HashSet<NastavnikPredmet>();

	
	public Ucenik() {
		super();
	}


	public Ucenik(String korisnickoIme, String lozinka, String ime, String prezime, String email,  String potvrdjenaLozinka, RoleEntity role, Integer id, List<Ocena> ocene, Roditelj roditelj, Set<NastavnikPredmet> nastavnikPredmet) {
		super(id, korisnickoIme, lozinka, ime, prezime, email, potvrdjenaLozinka, role);
		this.ocene = ocene;
		this.roditelj = roditelj;
		this.nastavnikPredmet = nastavnikPredmet;
		
	}

	public List<Ocena> getOcene() {
		return ocene;
	}


	public void setOcene(List<Ocena> ocene) {
		this.ocene = ocene;
	}



	public Roditelj getRoditelj() {
		return roditelj;
	}


	public void setRoditelj(Roditelj roditelj) {
		this.roditelj = roditelj;
	}


	public Set<NastavnikPredmet> getNastavnikPredmet() {
		return nastavnikPredmet;
	}


	public void setNastavnikPredmet(Set<NastavnikPredmet> nastavnikPredmet) {
		this.nastavnikPredmet = nastavnikPredmet;
	}



	

	

}
