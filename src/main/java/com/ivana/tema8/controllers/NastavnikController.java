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
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.Korisnik;
import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.NastavnikRepository;
import com.ivana.tema8.repositories.PredmetRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.NastavnikServiceImpl;

@RestController
@RequestMapping(path = "api/v1/nastavnik")
public class NastavnikController {

	@Autowired
	private NastavnikRepository nastavnikRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private NastavnikServiceImpl nastavnikService;

	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all nastavnici.");
		return new ResponseEntity<Iterable<Nastavnik>>(nastavnikRepository.findAll(), HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewNastavnik(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		return nastavnikService.addNewNastavnik(newUser, result);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteNastavnik(@PathVariable Integer id) {
		return nastavnikService.deleteNastavnik(id);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedNastavnik,
			BindingResult result) {
		return nastavnikService.updateUcenik(id, updatedNastavnik, result);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getNastavnikById(@PathVariable Integer id) {
		Optional<Nastavnik> nastavnik = nastavnikRepository.findById(id);

		if (!nastavnik.isPresent()) {
			logger.warn("Nije pronadjen nastavnik sa ID: {}", id);
			return new ResponseEntity<>("Nastavnik nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Nastavnik sa ID: {} je uspesno pronadjen", id);
		return new ResponseEntity<>(nastavnik.get(), HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by-name")
	public ResponseEntity<?> getNastavnikByName(@RequestParam String ime) {
		Optional<Nastavnik> nastavnik = nastavnikRepository.findByIme(ime);

		if (!nastavnik.isPresent()) {
			logger.warn("Nije pronadjen nastavnik sa imenom: {}", ime);
			return new ResponseEntity<>("Nastavnik nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Nastavnik sa imenom: {} je uspesno pronadjen", ime);
		return new ResponseEntity<>(nastavnik.get(), HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/predmet/{predmetId}")
	public ResponseEntity<?> getNastavniciByPredmetId(@PathVariable Integer predmetId) {
		Optional<Predmet> predmet = predmetRepository.findById(predmetId);
		if (!predmet.isPresent()) {
			logger.warn("Nije pronadjen predmet sa ID: {}", predmetId);
			return new ResponseEntity<>("Predmet nije pronadjen", HttpStatus.NOT_FOUND);
		}
		List<NastavnikPredmet> nastavnikPredmetList = nastavnikPredmetRepository.findByPredmet(predmet.get());
		List<Nastavnik> nastavnici = nastavnikPredmetList.stream().map(NastavnikPredmet::getNastavnik)
				.collect(Collectors.toList());
		logger.info("Nastavnici predmeta: sa ID: {} su uspesno pronadjeni", predmetId);
		return new ResponseEntity<>(nastavnici, HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/predmetIme")
	public ResponseEntity<?> getNastavniciByPredmetIme(@RequestParam String ime) {
		Optional<Predmet> predmet = predmetRepository.findByNazivPredmeta(ime);
		if (!predmet.isPresent()) {
			logger.warn("Nije pronadjen predmet sa imenom: {}", ime);
			return new ResponseEntity<>("Predmet nije pronadjen", HttpStatus.NOT_FOUND);
		}
		List<NastavnikPredmet> nastavnikPredmetList = nastavnikPredmetRepository.findByPredmet(predmet.get());
		List<Nastavnik> nastavnici = nastavnikPredmetList.stream().map(NastavnikPredmet::getNastavnik)
				.collect(Collectors.toList());
		logger.info("Nastavnici predmeta: {} su uspesno pronadjeni", ime);
		return new ResponseEntity<>(nastavnici, HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}



}
