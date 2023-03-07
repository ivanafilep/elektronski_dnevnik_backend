package com.ivana.tema8.entities;


import java.util.ArrayList;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class Roditelj extends Korisnik {
	
	
	@JsonIgnore
	@OneToMany(mappedBy = "roditelj", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<Ucenik> dete = new ArrayList<Ucenik>();
	
	
	public Roditelj() {
		super();
	}

	public Roditelj(String korisnickoIme, String lozinka, String ime, String prezime, String email,  String potvrdjenaLozinka, RoleEntity role, Integer id, List<Ucenik> dete) {
		super(id, korisnickoIme, lozinka, ime, prezime, email, potvrdjenaLozinka, role);
		this.dete = dete;
	}

	public List<Ucenik> getDete() {
		return dete;
	}

	public void setDete(List<Ucenik> dete) {
		this.dete = dete;
	}
	
	
}
