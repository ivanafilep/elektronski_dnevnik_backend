
package com.ivana.tema8.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ivana.tema8.dto.EmailDTO;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Ocena;
import com.ivana.tema8.entities.Polugodiste;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PolugodisteRepository;
import com.ivana.tema8.repositories.UcenikRepository;
import com.ivana.tema8.services.EmailServiceImpl;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.OcenaService;
import com.ivana.tema8.services.OcenaServiceImpl;

@RestController
@RequestMapping(path = "/api/v1/ocena")

public class OcenaController {

	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private PolugodisteRepository polugodisteRepository;
	@Autowired
	private OcenaServiceImpl ocenaService;
	@Autowired
	private EmailServiceImpl emailService;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all ocene.");
		return new ResponseEntity<Iterable<Ocena>>(ocenaRepository.findAll(), HttpStatus.OK);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateOcena(@PathVariable Integer id, @RequestParam Integer vrednostOcene,
			@RequestParam("nastavnikId") Integer nastavnikId, @RequestParam("predmetId") Integer predmetId, Authentication authentication) {
		return ocenaService.updateOcena(id, vrednostOcene, nastavnikId, predmetId, authentication);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.POST, path = "/{vrednostOcene}/{nastavnikPredmetId}/{ucenikId}/{polugodisteId}")
	public ResponseEntity<?> createOcena(@PathVariable Integer vrednostOcene, @PathVariable Integer nastavnikPredmetId,
			@PathVariable Integer ucenikId, @PathVariable Integer polugodisteId, Authentication authentication) {
		return ocenaService.createOcena(vrednostOcene, nastavnikPredmetId, ucenikId, polugodisteId, authentication);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK"})
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteOcena(@PathVariable Integer id, Authentication authentication) {
		return ocenaService.deleteOcena(id, authentication);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getOcenaById(@PathVariable Integer id, Authentication authentication) {
		Optional<Ocena> ocena = ocenaRepository.findById(id);

		if (!ocena.isPresent()) {
			logger.warn("Ocena sa ID: {} nije pronadjena", id);
			return new ResponseEntity<>("Ocena nije pronadjena", HttpStatus.NOT_FOUND);
		}
		logger.info("Nova ocena uspešno nadjena.");
		return new ResponseEntity<>(ocena.get(), HttpStatus.OK);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_UCENIK"})
	@RequestMapping(method = RequestMethod.GET, path = "/predmet")
	public ResponseEntity<?> findOcenaByPredmet(@RequestParam String nazivPredmeta, Authentication authentication) {
		logger.info("Ocene iz predmeta: {} su uspesno pronadjene", nazivPredmeta);
		return ocenaService.findOcenaByPredmet(nazivPredmeta, authentication);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK","ROLE_UCENIK", "ROLE_RODITELJ"})
	@RequestMapping(method = RequestMethod.GET, path = "/ime")
	public ResponseEntity<?> findOcenaByIme(@RequestParam String ime, Authentication authentication) {
		logger.info("Ocene od ucenika sa imenom: {} su uspesno pronadjene", ime);
		return ocenaService.findOcenaByIme(ime, authentication);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_RODITELJ", "ROLE_UCENIK"})
	@RequestMapping(method = RequestMethod.GET, path = "/imePredmet")
	public ResponseEntity<?> findOcenaByImePredmet(@RequestParam String ime, Authentication authentication) {
		logger.info("Ocene od ucenika sa imenom: {} su uspesno pronadjene", ime);
		return ocenaService.findOcenaByImePredmet(ime, authentication);
	}

	
	@Secured({"ROLE_ADMIN", "ROLE_NASTAVNIK", "ROLE_UCENIK", "ROLE_RODITELJ"})
	@RequestMapping(method = RequestMethod.GET, path = "/imeIPredmet")
	public ResponseEntity<?> findOcenaByPredmetIIme(@RequestParam String ime,
			@RequestParam String nazivPredmeta, Authentication authentication) {
		logger.info("Ocene od ucenika sa imenom: {}, iz predmeta: {} su uspesno pronadjene", ime, nazivPredmeta);
		return ocenaService.findByPredmetIIme(ime, nazivPredmeta, authentication);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));
		
	}

}

