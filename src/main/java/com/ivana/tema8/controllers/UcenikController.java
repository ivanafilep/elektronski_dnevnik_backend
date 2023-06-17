package com.ivana.tema8.controllers;

import java.util.ArrayList;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.annotation.JsonView;
import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.Korisnik;
import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Predmet;
import com.ivana.tema8.entities.Roditelj;
//import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.KorisnikRepository;
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
@CrossOrigin(origins = "http://localhost:3000")
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
	@Autowired
	private KorisnikRepository korisnikRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_RODITELJ"})
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll(Authentication authentication) {
	    String email = (String) authentication.getName();
	    Korisnik ulogovanKorisnik = korisnikRepository.findByEmail(email);

	    if (ulogovanKorisnik.getRole().getIme().equals("ROLE_ADMIN")) {
	        Iterable<Ucenik> ucenici = ucenikRepository.findAll();
	        return new ResponseEntity<>(ucenici, HttpStatus.OK);
	    } else if (ulogovanKorisnik.getRole().getIme().equals("ROLE_NASTAVNIK")) {
	        Nastavnik ulogovanNastavnik = (Nastavnik) ulogovanKorisnik;
	        List<Ucenik> ucenici = new ArrayList<>();

	        for (NastavnikPredmet nastavnikovPredmet : ulogovanNastavnik.getNastavnikPredmet()) {
	            ucenici.addAll(nastavnikovPredmet.getUcenici());
	        }
	        return new ResponseEntity<>(ucenici, HttpStatus.OK);
	        
	    } else if (ulogovanKorisnik.getRole().getIme().equals("ROLE_RODITELJ")) {
	        Roditelj ulogovanRoditelj = (Roditelj) ulogovanKorisnik;
	        List<Ucenik> ucenici = new ArrayList<>();

	        for (Ucenik dete : ucenikRepository.findAll()) {
	            if (dete.getRoditelj() != null && dete.getRoditelj().equals(ulogovanRoditelj)) {
	                ucenici.add(dete);
	            }
	        }


	        return new ResponseEntity<>(ucenici, HttpStatus.OK);
	    }

	    logger.info("Getting all ucenici");
	    return new ResponseEntity<Iterable<Ucenik>>(ucenikRepository.findAll(), HttpStatus.OK);
	}

	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewUcenik(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		return ucenikService.addNewUcenik(newUser, result);
	}

	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedUcenik,
			BindingResult result) {
		return ucenikService.updateUcenik(id, updatedUcenik, result);
	}

	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteUcenik(@PathVariable Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isEmpty()) {
			logger.warn("Zahtev sa brisanje ucenika sa nepostojecim ID {}", id);
			return new ResponseEntity<>(ucenik, HttpStatus.NOT_FOUND);
		} else {
			ucenikService.obrisiOceneUcenika(id);
			ucenikService.obrisiUcenika(id);
			logger.info("DELETE zahtev za brisanje ucenika sa ID {}", id);
			return new ResponseEntity<>(ucenik, HttpStatus.OK);
		}
	}
	

	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/ucenik/{ucenikId}/roditelj/{roditeljId}")
	public ResponseEntity<?> dodeliRoditeljaUceniku(@PathVariable Integer ucenikId, @PathVariable Integer roditeljId) {
		return ucenikService.dodeliRoditeljaUceniku(ucenikId, roditeljId);
	}

	
	@Secured("ROLE_ADMIN")
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

	
	@Secured("ROLE_ADMIN")
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

	
	@Secured("ROLE_ADMIN")
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

	
	@Secured("ROLE_ADMIN")
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

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{ucenikId}/nastavnikPredmet/{nastavnikPredmetId}")
	public ResponseEntity<?> dodeliNastavnikPredmetUceniku(@PathVariable Integer ucenikId,
			@PathVariable Integer nastavnikPredmetId) {
		return ucenikService.dodeliNastavnikPredmetUceniku(ucenikId, nastavnikPredmetId);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}



}
