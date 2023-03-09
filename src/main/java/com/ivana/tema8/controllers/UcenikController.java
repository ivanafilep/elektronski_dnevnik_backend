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
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Roditelj;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PredmetRepository;
import com.ivana.tema8.repositories.RoditeljRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.repositories.UcenikRepository;
import com.ivana.tema8.security.Views;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.UcenikServiceImpl;


@RestController
@RequestMapping(path = "api/v1/ucenik")
public class UcenikController {

	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private UcenikServiceImpl ucenikService;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	// ADMIN RADI OVO
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all ucenici");
		return new ResponseEntity<Iterable<Ucenik>>(ucenikRepository.findAll(), HttpStatus.OK);
	}

	// ADMIN RADI OVO
	// REGISTRACIJA UCENIKA
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewUcenik(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		return ucenikService.addNewUcenik(newUser, result);
	}

	// ADMIN RADI OVO
	// UPDATE UCENIKA
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedUcenik,
			BindingResult result) {
		return ucenikService.updateUcenik(id, updatedUcenik, result);
	}

	// ADMIN MOZE OVO
	// BRISANJE UCENIKA
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteUcenik(@PathVariable Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isEmpty()) {
			logger.warn("Zahtev sa brisanje ucenika sa nepostojecim ID {}", id);
			return new ResponseEntity<>("Ucenik sa zatrazenim ID- jem ne postoji.", HttpStatus.NOT_FOUND);
		} else {
			ucenikService.obrisiOceneUcenika(id);
			ucenikService.obrisiUcenika(id);
			logger.info("DELETE zahtev za brisanje ucenika sa ID {}", id);
			return new ResponseEntity<>("Ucenik je uspesno obrisan", HttpStatus.OK);
		}
	}

	// ADMIN RADI OVO
	// DODELA RODITELJA UCENIKU
	@RequestMapping(method = RequestMethod.PUT, path = "/ucenik/{ucenikId}/roditelj/{roditeljId}")
	public ResponseEntity<?> dodeliRoditeljaUceniku(@PathVariable Integer ucenikId, @PathVariable Integer roditeljId) {
		return ucenikService.dodeliRoditeljaUceniku(ucenikId, roditeljId);
	}

	// PRETRAGA PO ID
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getUcenikById(@PathVariable Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);

		if (!ucenik.isPresent()) {
			logger.warn("Nije pronadjen ucenik sa ID: {}", id);
			return new ResponseEntity<>("Ucenik nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Ucenik sa ID: {} je uspesno pronadjen", id);
		return new ResponseEntity<>(ucenik.get(), HttpStatus.OK);
	}

	// PRETRAGA PO IMENU UCENIKA
	@RequestMapping(method = RequestMethod.GET, path = "/by-name")
	public ResponseEntity<?> getUcenikByName(@RequestParam String ime) {
		Optional<Ucenik> ucenik = ucenikRepository.findByIme(ime);

		if (!ucenik.isPresent()) {
			logger.warn("Nije pronadjen ucenik sa imenom: {}", ime);
			return new ResponseEntity<>("Ucenik nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Ucenik sa imenom: {} je uspesno pronadjen", ime);
		return new ResponseEntity<>(ucenik.get(), HttpStatus.OK);
	}

	// nadji ucenika po id-u roditelja
	@RequestMapping(method = RequestMethod.GET, path = "/roditelj/{roditeljId}")
	public ResponseEntity<?> getUceniciByRoditeljId(@PathVariable Integer roditeljId) {
		Optional<Roditelj> roditelj = roditeljRepository.findById(roditeljId);
		if (!roditelj.isPresent()) {
			logger.warn("Nije pronadjen roditelj sa ID: {}", roditeljId);
			return new ResponseEntity<>("Roditelj nije pronadjen", HttpStatus.NOT_FOUND);
		}
		List<Ucenik> ucenici = roditelj.get().getDete();
		logger.info("Ucenik sa ID roditelja: {} je uspesno pronadjen", roditeljId);
		return new ResponseEntity<>(ucenici, HttpStatus.OK);
	}

	// nadji ucenike ciji se roditelj zove nekako
	@RequestMapping(method = RequestMethod.GET, path = "/roditelj")
	public ResponseEntity<?> getUceniciByImeRoditelja(@RequestParam String imeRoditelja) {
		Optional<Roditelj> roditelj = roditeljRepository.findByIme(imeRoditelja);
		if (roditelj.isEmpty()) {
			logger.warn("Nije pronadjen ucenik sa imenom roditelja: {}", imeRoditelja);
			return new ResponseEntity<>("Roditelj nije pronađen", HttpStatus.NOT_FOUND);
		}

		List<Ucenik> ucenici = roditelj.get().getDete();
		logger.info("Ucenik sa imenom roditelja: {} je uspesno pronadjen", imeRoditelja);
		return new ResponseEntity<>(ucenici, HttpStatus.OK);
	}

	// DODELA NASTAVNIKPREDMET UCENIKU
	@RequestMapping(method = RequestMethod.PUT, path = "/{ucenikId}/nastavnikPredmet/{nastavnikPredmetId}")
	public ResponseEntity<?> dodeliNastavnikPredmetUceniku(@PathVariable Integer ucenikId,
			@PathVariable Integer nastavnikPredmetId) {
		return ucenikService.dodeliNastavnikPredmetUceniku(ucenikId, nastavnikPredmetId);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
	
	
	/*
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewUcenik (@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		Ucenik newUcenik = new Ucenik();
		RoleEntity roleEntity = roleRepository.findById(2).orElse(null);

		newUcenik.setKorisnickoIme(newUser.getKorisnickoIme());
		newUcenik.setIme(newUser.getIme());
		newUcenik.setPrezime(newUser.getPrezime());
		newUcenik.setEmail(newUser.getEmail());
		
		if (newUser.getPotvrdjenaLozinka().equals(newUser.getLozinka())) {
			newUcenik.setLozinka(newUser.getLozinka());
			
		} else {
			return new ResponseEntity<>("Lozinke se ne poklapaju! Molimo unesite opet.", HttpStatus.BAD_REQUEST);
		}

		logger.info("Novi ucenik uspešno dodat.");
		newUcenik.setRole(roleEntity);
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		ucenikRepository.save(newUcenik);
		logger.info("Novi ucenik uspešno dodat.");
		return new ResponseEntity<>(newUcenik, HttpStatus.CREATED);
	}
	
	
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedUcenik, BindingResult result) {
		logger.info("Pokušaj izmene učenika sa id-jem {}", id);
		Ucenik ucenik = ucenikRepository.findById(id).get();
		
		ucenik.setKorisnickoIme(updatedUcenik.getKorisnickoIme());
		ucenik.setLozinka(updatedUcenik.getLozinka());
		ucenik.setIme(updatedUcenik.getIme());
		ucenik.setPrezime(updatedUcenik.getPrezime());
		ucenik.setEmail(updatedUcenik.getEmail());
		
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		ucenikRepository.save(ucenik);
		logger.info("Učenik sa id-jem {} je uspešno izmenjen", id);
		return new ResponseEntity<>(ucenik, HttpStatus.OK);
	}
	
	
	// dodela roditelja uceniku
	@RequestMapping(method = RequestMethod.PUT, path = "/ucenik/{ucenikId}/roditelj/{roditeljId}")
	public ResponseEntity<?> dodeliRoditeljaUceniku (@PathVariable Integer ucenikId, @PathVariable Integer roditeljId) {
		
		Ucenik ucenik = ucenikRepository.findById(ucenikId).orElse(null);
		Roditelj roditelj = roditeljRepository.findById(roditeljId).orElse(null);
		
		if (ucenik == null || roditelj == null) {
	        return new ResponseEntity<>("Nastavnik ili predmet nisu pronađeni", HttpStatus.NOT_FOUND);
	    }
		
		ucenik.setRoditelj(roditelj);
		ucenikRepository.save(ucenik);
		logger.info("Roditelj je uspesno dodeljen uceniku.");
		return new ResponseEntity<>("Roditelj je uspešno dodeljen uceniku", HttpStatus.OK);
	}
	

	//dodaj nastavnik predmet
	@RequestMapping(method = RequestMethod.PUT, path = "/{ucenikId}/nastavnikPredmet/{nastavnikPredmetId}")
	public ResponseEntity<?> dodeliNastavnikPredmetUceniku(@PathVariable Integer ucenikId, @PathVariable Integer nastavnikPredmetId) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(ucenikId);
		Optional<NastavnikPredmet> nastavnikPredmet = nastavnikPredmetRepository.findById(nastavnikPredmetId);

		if (ucenik.isPresent() && nastavnikPredmet.isPresent()) {
		    nastavnikPredmet.get().getUcenici().add(ucenik.get());
		    nastavnikPredmetRepository.save(nastavnikPredmet.get());
		    logger.info("Predmet je uspesno dodeljen uceniku");
		    return new ResponseEntity<>("Predmet je uspešno dodeljen uceniku", HttpStatus.OK);
		} else {
			logger.warn("Ucenik ili nastavnik-predmet nisu pronadjeni");
		    return new ResponseEntity<>("Ucenik ili nastavnik-predmet nisu pronađeni", HttpStatus.NOT_FOUND);
		}

	}
	*/



}
