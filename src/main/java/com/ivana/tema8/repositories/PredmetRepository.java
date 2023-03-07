package com.ivana.tema8.repositories;



import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import com.ivana.tema8.entities.Predmet;

public interface PredmetRepository extends CrudRepository<Predmet, Integer> {

	Optional<Predmet> findByNazivPredmeta(String ime);

	
	

	

}
