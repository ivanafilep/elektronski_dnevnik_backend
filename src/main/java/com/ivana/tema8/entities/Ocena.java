package com.ivana.tema8.entities;

import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
@Valid
public class Ocena {
	
	@Id
	@GeneratedValue
	private Integer id;
	@Column
	@NotNull(message = "Ocena mora biti navedena.")
	private Integer vrednostOcene;
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "nastavnikPredmet")
	@JsonProperty("nastavnikPredmet")
	private NastavnikPredmet nastavnikPredmet;
	
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "ucenik")
	@JsonProperty("ucenik")
	private Ucenik ucenik;
	
	
	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	@JoinColumn(name = "polugodiste")
	@JsonProperty("polugodiste")
	private Polugodiste polugodiste;
	
	
	public Ocena() {
		super();
	}

	public Ocena(Integer id, Integer vrednostOcene, NastavnikPredmet nastavnikPredmet, Ucenik ucenik, Polugodiste polugodiste) {
		super();
		this.id = id;
		this.vrednostOcene = vrednostOcene;
		this.nastavnikPredmet = nastavnikPredmet;
		this.ucenik = ucenik;
		this.polugodiste = polugodiste;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getVrednostOcene() {
		return vrednostOcene;
	}

	public void setVrednostOcene(Integer vrednostOcene) {
		this.vrednostOcene = vrednostOcene;
	}



	public Ucenik getUcenik() {
		return ucenik;
	}

	public void setUcenik(Ucenik ucenik) {
		this.ucenik = ucenik;
	}

	public NastavnikPredmet getNastavnikPredmet() {
		return nastavnikPredmet;
	}

	public void setNastavnikPredmet(NastavnikPredmet nastavnikPredmet) {
		this.nastavnikPredmet = nastavnikPredmet;
	}

	public Polugodiste getPolugodiste() {
		return polugodiste;
	}

	public void setPolugodiste(Polugodiste polugodiste) {
		this.polugodiste = polugodiste;
	}

	
	
	
	
	

}
