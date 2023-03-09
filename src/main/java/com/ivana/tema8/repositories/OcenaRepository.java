package com.ivana.tema8.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;

public interface OcenaRepository extends CrudRepository<Ocena, Integer> {

	List<Ocena> findByNastavnikPredmet(NastavnikPredmet nastavnikPredmet);

}
