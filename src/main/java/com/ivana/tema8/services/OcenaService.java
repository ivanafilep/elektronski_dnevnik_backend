package com.ivana.tema8.services;

import org.springframework.http.ResponseEntity;


import org.springframework.security.core.Authentication;

import com.ivana.tema8.entities.Ocena;

public interface OcenaService {

	//ResponseEntity<?> findOcenaByPredmet(String nazivPredmeta);

	ResponseEntity<?> findOcenaByIme(String ime, Authentication authentication);

	//ResponseEntity<?> findOcenaByImePredmet(String ime);
	
	//ResponseEntity<?> findByPredmetIIme (String ime, String nazivPredmeta);

	ResponseEntity<?> findOcenaByPredmet(String nazivPredmeta, Authentication authentication);

	ResponseEntity<?> findOcenaByImePredmet(String ime, Authentication authentication);


	


}
