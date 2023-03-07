package com.ivana.tema8.services;

import com.ivana.tema8.entities.Ocena;

public interface OcenaService {

	Iterable<Ocena> findOcenaByPredmet(String nazivPredmeta);

	Iterable<Ocena> findOcenaByIme(String ime);

	Iterable<Ocena> findOcenaByImePredmet(String ime);
	
	Iterable<Ocena> findByPredmetIIme (String ime, String nazivPredmeta);


}
