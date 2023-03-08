package com.ivana.tema8.entities;
import javax.persistence.Entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties ({"hibernateLazyInitializer", "handler"})
public class Admin extends Korisnik {
	
	
	public Admin() {
		super();
	}
	
	public Admin(String korisnickoIme, String lozinka, String ime, String prezime, String email, RoleEntity role, Integer id) {
		super(id, korisnickoIme, lozinka, ime, prezime, email, role);
		
	}

	
}
