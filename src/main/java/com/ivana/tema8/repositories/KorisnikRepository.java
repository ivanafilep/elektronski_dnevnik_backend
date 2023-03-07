package com.ivana.tema8.repositories;



import org.springframework.data.repository.CrudRepository;

import com.ivana.tema8.entities.Korisnik;

public interface KorisnikRepository extends CrudRepository<Korisnik, Integer> {

	public Korisnik findByEmail(String email);

}
