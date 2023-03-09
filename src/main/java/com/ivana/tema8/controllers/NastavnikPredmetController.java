package com.ivana.tema8.controllers;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all nastavnikPredmet.");
		return new ResponseEntity<Iterable<NastavnikPredmet>>(nastavnikPredmetRepository.findAll(), HttpStatus.OK);
	}

	// DODELI PREDMET NASTAVNIKU
	@RequestMapping(method = RequestMethod.PUT, path = "/nastavnik/{nastavnikId}/predmet/{predmetId}/{razred}")
	public ResponseEntity<?> dodeliPredmetNastavniku(@PathVariable Integer nastavnikId, @PathVariable Integer predmetId,
			@PathVariable Integer razred) {
		return nastavnikPredmetService.dodeliPredmetNastavniku(nastavnikId, predmetId, razred);
	}

	// OBRISI
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> obrisiNastavnikPredmet(@PathVariable Integer id) {
		return nastavnikPredmetService.obrisiNastavnikPredmet(id);
	}
	/*
	public ResponseEntity<?> dodeliPredmetNastavniku(@PathVariable Integer nastavnikId, @PathVariable Integer predmetId, @PathVariable Integer razred) {
		
	    Optional<Nastavnik> nastavnik = nastavnikRepository.findById(nastavnikId);
	    Optional<Predmet> predmet = predmetRepository.findById(predmetId);

	    if (!nastavnik.isPresent() || !predmet.isPresent()) {
	        return new ResponseEntity<>("Nastavnik ili predmet nisu pronađeni", HttpStatus.NOT_FOUND);
	    }

	    NastavnikPredmet nastavnikPredmet = new NastavnikPredmet();
	    nastavnikPredmet.setNastavnik(nastavnik.get());
	    nastavnikPredmet.setPredmet(predmet.get());
	    nastavnikPredmet.setRazred(razred);
	    nastavnikPredmetRepository.save(nastavnikPredmet);

	    logger.info("Predmet sa ID: {}, je uspešno dodeljen nastavniku sa ID: {}, u razredu sa ID: {}", predmetId, nastavnikId, razred );
	    return new ResponseEntity<>("Predmet je uspešno dodeljen nastavniku", HttpStatus.OK);
	    
	    
	}

	public ResponseEntity<?> obrisiNastavnikPredmet(@PathVariable Integer id) {
	    Optional<NastavnikPredmet> nastavnikPredmet = nastavnikPredmetRepository.findById(id);
	    if (!nastavnikPredmet.isPresent()) {
	        logger.warn("Zahtev sa brisanje nastavnikPredmet sa nepostojecim ID {}", id);
	        return new ResponseEntity<>("NastavnikPredmet nije pronađen", HttpStatus.NOT_FOUND);
	    }
	    List<Ocena> ocene = ocenaRepository.findByNastavnikPredmet(nastavnikPredmet.get());

	    ocenaRepository.deleteAll(ocene);

	    logger.info("DELETE zahtev za brisanje nastavnikPredmeta sa ID {}", id);
	    nastavnikPredmetRepository.delete(nastavnikPredmet.get());
	    return new ResponseEntity<>("NastavnikPredmet je uspešno obrisan", HttpStatus.OK);
	}

*/
	

}
