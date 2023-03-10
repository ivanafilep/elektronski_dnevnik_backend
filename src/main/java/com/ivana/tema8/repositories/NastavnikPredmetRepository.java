package com.ivana.tema8.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Predmet;

public interface NastavnikPredmetRepository extends CrudRepository<NastavnikPredmet, Integer> {

	List<NastavnikPredmet> findByPredmet(Predmet predmet);

	List<NastavnikPredmet> findByNastavnik(Nastavnik nastavnik);

	NastavnikPredmet findByNastavnikIdAndPredmetId(Integer nastavnikId, Integer predmetId);

	List<NastavnikPredmet> findByNastavnikId(Integer id);

	



	



	

	

}
