package com.ivana.tema8.entities;

import java.util.ArrayList;



import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class Nastavnik extends Korisnik {
	

	
	@JsonIgnore
	@OneToMany(mappedBy = "nastavnik", cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private List<NastavnikPredmet> nastavnikPredmet = new ArrayList<NastavnikPredmet>();
	
	public Nastavnik() {
		super();
	}

	public Nastavnik(String korisnickoIme, String lozinka, String ime, String prezime, String email, /*RoleEntity role, */Integer id, List<NastavnikPredmet> nastavnikPredmet ) {
		super(id, korisnickoIme, lozinka, ime, prezime, email/*, role*/);
		this.nastavnikPredmet = nastavnikPredmet;
		
	}

	public List<NastavnikPredmet> getNastavnikPredmet() {
		return nastavnikPredmet;
	}

	public void setNastavnikPredmet(List<NastavnikPredmet> nastavnikPredmet) {
		this.nastavnikPredmet = nastavnikPredmet;
	}



	
}
