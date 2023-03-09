package com.ivana.tema8.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class NastavnikPredmet {

	@Id
	@GeneratedValue
	private Integer id;
	@Column
	private Integer razred;
	

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "predmet")
	private Predmet predmet;
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nastavnik")
	private Nastavnik nastavnik;
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "nastavnikPredmet", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Ocena> ocene = new ArrayList<Ocena>();
	
	@JsonIgnore
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	@JoinTable(name = "Ucenik_NastavnikPredmet", joinColumns = 
		{@JoinColumn(name = "NastavnikPredmet_id", nullable = false, updatable = false)},
		inverseJoinColumns = {@JoinColumn(name = "ucenik_id",
			nullable = false, updatable = false) })
	protected Set<Ucenik> ucenici = new HashSet<Ucenik>();
	
	
	public NastavnikPredmet() {
		super();
	}
	public NastavnikPredmet(Integer razred, Predmet predmet, Nastavnik nastavnik, List<Ocena> ocene, Set<Ucenik> ucenici) {
		super();
		this.razred = razred;
		this.predmet = predmet;
		this.nastavnik = nastavnik;
		this.ocene = ocene;
		this.ucenici = ucenici;
	}
	
	public Integer getRazred() {
		return razred;
	}
	public void setRazred(Integer razred) {
		this.razred = razred;
	}
	public Predmet getPredmet() {
		return predmet;
	}
	public void setPredmet(Predmet predmet) {
		this.predmet = predmet;
	}
	public Nastavnik getNastavnik() {
		return nastavnik;
	}
	public void setNastavnik(Nastavnik nastavnik) {
		this.nastavnik = nastavnik;
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
	public Set<Ucenik> getUcenici() {
		return ucenici;
	}
	public void setUcenici(Set<Ucenik> ucenici) {
		this.ucenici = ucenici;
	}


	
	

	
}
