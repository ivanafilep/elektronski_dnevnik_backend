package com.ivana.tema8.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.NastavnikRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PredmetRepository;

@Service
public class NastavnikPredmetServiceImpl implements NastavnikPredmetService {

	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private OcenaRepository ocenaRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	public ResponseEntity<?> dodeliPredmetNastavniku(@PathVariable Integer nastavnikId, @PathVariable Integer predmetId,
			@PathVariable Integer razred) {

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

		logger.info("Predmet sa ID: {}, je uspešno dodeljen nastavniku sa ID: {}, u razredu sa ID: {}", predmetId,
				nastavnikId, razred);
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

}
