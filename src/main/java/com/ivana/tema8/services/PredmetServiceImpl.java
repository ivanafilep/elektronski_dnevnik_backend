package com.ivana.tema8.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.ivana.tema8.dto.PredmetDTO;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.NastavnikRepository;
import com.ivana.tema8.repositories.PredmetRepository;

@Service
public class PredmetServiceImpl implements PredmetService {

	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	public ResponseEntity<?> addNewUPredmet(@Valid @RequestBody PredmetDTO newPredmet, BindingResult result) {
		Predmet newPredmet1 = new Predmet();

		newPredmet1.setNazivPredmeta(newPredmet.getNazivPredmeta());
		newPredmet1.setNedeljniFondCasova(newPredmet.getNedeljniFondCasova());
		logger.info("Dodavanje novog predmeta.");

		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
			logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		predmetRepository.save(newPredmet1);
		logger.info("Novi predmet uspešno dodat.");
		return new ResponseEntity<>(newPredmet1, HttpStatus.CREATED);
	}

	
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updatePredmet(@PathVariable Integer id, @Valid @RequestBody PredmetDTO updatedPredmet,
			BindingResult result) {
		logger.info("Pokušaj izmene predmeta sa id-jem {}", id);
		Predmet predmet = predmetRepository.findById(id).get();

		predmet.setNazivPredmeta(updatedPredmet.getNazivPredmeta());
		predmet.setNedeljniFondCasova(updatedPredmet.getNedeljniFondCasova());

		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
			logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		predmetRepository.save(predmet);
		logger.info("Predmet sa id-jem {} je uspešno izmenjen", id);
		return new ResponseEntity<>(predmet, HttpStatus.OK);
	}

	
	public ResponseEntity<?> deletePredmet(@PathVariable Integer id) {
		Optional<Predmet> predmet = predmetRepository.findById(id);
		if (predmet.isEmpty()) {
			logger.warn("Zahtev sa brisanje predmeta sa nepostojecim ID {}", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {

			List<NastavnikPredmet> nastavnikPredmeti = nastavnikPredmetRepository.findByPredmet(predmet.get());

			for (NastavnikPredmet np : nastavnikPredmeti) {
				np.setPredmet(null);
				nastavnikPredmetRepository.save(np);
			}
			logger.info("DELETE zahtev za brisanje predmeta sa ID {}", id);
			predmetRepository.delete(predmet.get());
			return new ResponseEntity<>("Predmet je uspesno obrisan", HttpStatus.OK);
		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
	

}
