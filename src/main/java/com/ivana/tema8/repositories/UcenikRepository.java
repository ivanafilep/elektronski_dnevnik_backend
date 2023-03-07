package com.ivana.tema8.repositories;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ivana.tema8.entities.Ucenik;

public interface UcenikRepository extends CrudRepository<Ucenik, Integer> {

	Optional<Ucenik> findByIme(String ime);



	

}
