package com.ivana.tema8.controllers;

import java.util.List;



import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ivana.tema8.dto.PredmetDTO;
import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.NastavnikRepository;
import com.ivana.tema8.repositories.PredmetRepository;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.PredmetServiceImpl;

@RestController
@RequestMapping(path = "/api/v1/predmet")
@CrossOrigin(origins = "http://localhost:3000")
public class PredmetController {

	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private PredmetServiceImpl predmetService;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all predmeti");
		return new ResponseEntity<Iterable<Predmet>>(predmetRepository.findAll(), HttpStatus.OK);
	}

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewUPredmet(@Valid @RequestBody PredmetDTO newPredmet, BindingResult result) {
		return predmetService.addNewUPredmet(newPredmet, result);
	}

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updatePredmet(@PathVariable Integer id, @Valid @RequestBody PredmetDTO updatedPredmet,
			BindingResult result) {
		return predmetService.updatePredmet(id, updatedPredmet, result);
	}


	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deletePredmet(@PathVariable Integer id) {
		return predmetService.deletePredmet(id);
	}

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getPredmetById(@PathVariable Integer id) {
		Optional<Predmet> predmet = predmetRepository.findById(id);

		if (!predmet.isPresent()) {
			logger.warn("Nije pronadjen predmet sa ID: {}", id);
			return new ResponseEntity<>("Predmet nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Predmet sa ID: {} je uspesno pronadjen", id);
		return new ResponseEntity<>(predmet.get(), HttpStatus.OK);
	}

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by-name")
	public ResponseEntity<?> getPredmetByName(@RequestParam String ime) {
		Optional<Predmet> predmet = predmetRepository.findByNazivPredmeta(ime);

		if (!predmet.isPresent()) {
			logger.warn("Nije pronadjen predmet sa imenom: {}", ime);
			return new ResponseEntity<>("Predmet nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Predmet sa imenom: {} je uspesno pronadjen", ime);
		return new ResponseEntity<>(predmet.get(), HttpStatus.OK);
	}

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/nastavnik/{nastavnikId}")
	public ResponseEntity<?> getPredmetByNastavnikId(@PathVariable Integer nastavnikId) {
		Optional<Nastavnik> nastavnik = nastavnikRepository.findById(nastavnikId);
		if (!nastavnik.isPresent()) {
			logger.warn("Nije pronadjen nastavnik sa ID: {}", nastavnikId);
			return new ResponseEntity<>("Nastavnik nije pronadjen", HttpStatus.NOT_FOUND);
		}
		List<NastavnikPredmet> nastavnikPredmetList = nastavnikPredmetRepository.findByNastavnik(nastavnik.get());
		List<Predmet> predmeti = nastavnikPredmetList.stream().map(NastavnikPredmet::getPredmet)
				.collect(Collectors.toList());
		logger.info("Predmet sa ID: {} je uspesno pronadjen", nastavnikId);
		return new ResponseEntity<>(predmeti, HttpStatus.OK);
	}

	
	//@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/nastavnikIme")
	public ResponseEntity<?> getPredmetByNastavnikIme(@RequestParam String ime) {
		Optional<Nastavnik> nastavnik = nastavnikRepository.findByIme(ime);
		if (!nastavnik.isPresent()) {
			logger.warn("Nije pronadjen nastavnik sa imenom: {}", ime);
			return new ResponseEntity<>("Nastavnik nije pronadjen", HttpStatus.NOT_FOUND);
		}
		List<NastavnikPredmet> nastavnikPredmetList = nastavnikPredmetRepository.findByNastavnik(nastavnik.get());
		List<Predmet> predmeti = nastavnikPredmetList.stream().map(NastavnikPredmet::getPredmet)
				.collect(Collectors.toList());
		logger.info("Predmet sa imenom nastavnika: {} je uspesno pronadjen", ime);
		return new ResponseEntity<>(predmeti, HttpStatus.OK);

	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}

	
		
		
		

}
