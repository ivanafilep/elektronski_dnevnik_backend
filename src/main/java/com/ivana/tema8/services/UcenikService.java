package com.ivana.tema8.services;

import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.UcenikRepository;

@Service
public class UcenikService {
	
	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private OcenaRepository ocenaRepository;
	

	public void obrisiOceneUcenika(Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isPresent()) {
			ucenik.get().getOcene().forEach(ocena -> ocenaRepository.delete(ocena));
		}
	}

	public void obrisiUcenika(Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isPresent()) {
			ucenikRepository.delete(ucenik.get());
		}
	}

	   
	

	


}
