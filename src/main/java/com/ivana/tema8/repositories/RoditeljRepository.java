package com.ivana.tema8.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ivana.tema8.entities.Roditelj;

public interface RoditeljRepository extends CrudRepository<Roditelj, Integer> {

	Optional<Roditelj> findByIme(String ime);

}
