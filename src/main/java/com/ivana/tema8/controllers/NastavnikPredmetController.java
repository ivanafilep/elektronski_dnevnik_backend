package com.ivana.tema8.controllers;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.NastavnikRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PredmetRepository;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.NastavnikPredmetServiceImpl;

@RestController
@RequestMapping(path = "/api/v1/nastavnikPredmet")
public class NastavnikPredmetController {

	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private NastavnikPredmetServiceImpl nastavnikPredmetService;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all nastavnikPredmet.");
		return new ResponseEntity<Iterable<NastavnikPredmet>>(nastavnikPredmetRepository.findAll(), HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/nastavnik/{nastavnikId}/predmet/{predmetId}/{razred}")
	public ResponseEntity<?> dodeliPredmetNastavniku(@PathVariable Integer nastavnikId, @PathVariable Integer predmetId,
			@PathVariable Integer razred) {
		return nastavnikPredmetService.dodeliPredmetNastavniku(nastavnikId, predmetId, razred);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> obrisiNastavnikPredmet(@PathVariable Integer id) {
		return nastavnikPredmetService.obrisiNastavnikPredmet(id);
	}


}
