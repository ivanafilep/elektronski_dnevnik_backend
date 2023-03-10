package com.ivana.tema8.services;

import org.springframework.http.ResponseEntity;

import com.ivana.tema8.entities.Ocena;

public interface OcenaService {

	ResponseEntity<?> findOcenaByPredmet(String nazivPredmeta);

	ResponseEntity<?> findOcenaByIme(String ime);

	ResponseEntity<?> findOcenaByImePredmet(String ime);
	
	ResponseEntity<?> findByPredmetIIme (String ime, String nazivPredmeta);


}
