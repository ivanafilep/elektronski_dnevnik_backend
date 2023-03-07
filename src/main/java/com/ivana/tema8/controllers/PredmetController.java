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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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

@RestController
@RequestMapping(path = "/api/v1/predmet")
public class PredmetController {
	
	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired 
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	//ADMIN RADI OVO
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all predmeti");
		return new ResponseEntity<Iterable<Predmet>>(predmetRepository.findAll(), HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewUPredmet (@Valid @RequestBody PredmetDTO newPredmet, BindingResult result) {
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
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
	
	//ADMIN MOZE OVO
		@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
		public ResponseEntity<?> deletePredmet (@PathVariable Integer id) {
			Optional<Predmet> predmet = predmetRepository.findById(id);
				if (predmet.isEmpty()) {
					logger.warn("Zahtev sa brisanje predmeta sa nepostojecim ID {}", id);
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				} else {
					logger.info("DELETE zahtev za brisanje predmeta sa ID {}", id);
					predmetRepository.delete(predmet.get());
					return new ResponseEntity<>(HttpStatus.OK);
					
				}			
		}
		
		@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
		public ResponseEntity<?> updatePredmet(@PathVariable Integer id, @Valid @RequestBody PredmetDTO updatedPredmet, BindingResult result) {
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
		
		@RequestMapping(method = RequestMethod.GET, path = "/{id}")
		public ResponseEntity<?> getPredmetById (@PathVariable Integer id) {
			Optional<Predmet> predmet = predmetRepository.findById(id);
			
			if (!predmet.isPresent()) {
				logger.warn("Nije pronadjen predmet sa ID: {}", id);
		        return new ResponseEntity<>("Predmet nije pronadjen", HttpStatus.NOT_FOUND);
		    }
			logger.info("Predmet sa ID: {} je uspesno pronadjen", id);
		    return new ResponseEntity<>(predmet.get(), HttpStatus.OK);
		}
		
		@RequestMapping(method = RequestMethod.GET, path = "/by-name")
		public ResponseEntity<?> getPredmetByName (@RequestParam String ime) {
			Optional<Predmet> predmet = predmetRepository.findByNazivPredmeta(ime);
			
			if (!predmet.isPresent()) {
				logger.warn("Nije pronadjen predmet sa imenom: {}", ime);
		        return new ResponseEntity<>("Predmet nije pronadjen", HttpStatus.NOT_FOUND);
		    }
			logger.info("Predmet sa imenom: {} je uspesno pronadjen", ime);
		    return new ResponseEntity<>(predmet.get(), HttpStatus.OK);
		}
		
		
		//nadji predmet po id-u nastavnika
		@RequestMapping(method = RequestMethod.GET, path = "/nastavnik/{nastavnikId}")
		public ResponseEntity<?> getPredmetByNastavnikId(@PathVariable Integer nastavnikId) {
		    Optional<Nastavnik> nastavnik = nastavnikRepository.findById(nastavnikId);
		    if (!nastavnik.isPresent()) {
		    	logger.warn("Nije pronadjen nastavnik sa ID: {}", nastavnikId);
		        return new ResponseEntity<>("Nastavnik nije pronadjen", HttpStatus.NOT_FOUND);
		    }
		    List<NastavnikPredmet> nastavnikPredmetList = nastavnikPredmetRepository.findByNastavnik(nastavnik.get());
		    List<Predmet> predmeti = nastavnikPredmetList.stream()
		            .map(NastavnikPredmet::getPredmet)
		            .collect(Collectors.toList());
		    logger.info("Predmet sa ID: {} je uspesno pronadjen", nastavnikId);
		    return new ResponseEntity<>(predmeti, HttpStatus.OK);
		}

		//nadji predmet po imenu nastavnika 
		@RequestMapping(method = RequestMethod.GET, path = "/nastavnikIme")
		public ResponseEntity<?> getPredmetByNastavnikIme (@RequestParam String ime) {
			Optional<Nastavnik> nastavnik = nastavnikRepository.findByIme(ime);
			if(!nastavnik.isPresent()) {
				logger.warn("Nije pronadjen nastavnik sa imenom: {}", ime);
				return new ResponseEntity<>("Nastavnik nije pronadjen", HttpStatus.NOT_FOUND);
			}
			List<NastavnikPredmet> nastavnikPredmetList = nastavnikPredmetRepository.findByNastavnik(nastavnik.get());
			List<Predmet> predmeti = nastavnikPredmetList.stream()
			            .map(NastavnikPredmet::getPredmet)
			            .collect(Collectors.toList());
			logger.info("Predmet sa imenom nastavnika: {} je uspesno pronadjen", ime);
			    return new ResponseEntity<>(predmeti, HttpStatus.OK);
			
		}
		
		
		

}