package com.ivana.tema8.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ivana.tema8.entities.Nastavnik;

public interface NastavnikRepository extends CrudRepository<Nastavnik, Integer> {

	Optional<Nastavnik> findByIme(String ime);

}
