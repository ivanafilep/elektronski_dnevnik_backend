package com.ivana.tema8.controllers;

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
import com.ivana.tema8.entities.Roditelj;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.RoditeljRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.repositories.UcenikRepository;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.RoditeljService;
import com.ivana.tema8.services.RoditeljServiceImpl;

@RestController
@RequestMapping(path = "api/v1/roditelj")
public class RoditeljController {

	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private RoditeljServiceImpl roditeljService;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all roditelji");
		return new ResponseEntity<Iterable<Roditelj>>(roditeljRepository.findAll(), HttpStatus.OK);
	}

	
	// REGISTRACIJA RODITELJA
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewRoditelj(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		return roditeljService.addNewRoditelj(newUser, result);
	}

	
	// UPDATE RODITELJA
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateRoditelj(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedRoditelj,
			BindingResult result) {
		return roditeljService.updateRoditelj(id, updatedRoditelj, result);
	}

	
	// BRISANJE RODITELJA
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRoditelj(@PathVariable Integer id) {
		return roditeljService.deleteRoditelj(id);
	}

	// NADJI RODITELJA PO ID
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getRoditeljkById(@PathVariable Integer id) {
		Optional<Roditelj> roditelj = roditeljRepository.findById(id);

		if (!roditelj.isPresent()) {
			logger.warn("Nije pronadjen roditelj sa ID: {}", id);
			return new ResponseEntity<>("Roditelj nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Roditelj sa ID: {} je uspesno pronadjen", id);
		return new ResponseEntity<>(roditelj.get(), HttpStatus.OK);
	}

	// NADJI RODITELJA PO IMENU
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by-name")
	public ResponseEntity<?> getRoditeljByName(@RequestParam String ime) {
		Optional<Roditelj> roditelj = roditeljRepository.findByIme(ime);

		if (!roditelj.isPresent()) {
			logger.warn("Nije pronadjen roditelj sa imenom: {}", ime);
			return new ResponseEntity<>("Roditelj nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Roditelj sa imenom: {} je uspesno pronadjen", ime);
		return new ResponseEntity<>(roditelj.get(), HttpStatus.OK);
	}

	// nadji roditelja po id-u ucenika
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/ucenik/{id}")
	public ResponseEntity<?> getUcenikById(@PathVariable Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isPresent()) {
			logger.info("Roditelj sa ID ucenika: {} je uspesno pronadjen", id);
			return new ResponseEntity<>(ucenik, HttpStatus.OK);
		} else {
			logger.warn("Nije pronadjen ucenik sa ID: {}", id);
			return new ResponseEntity<>("Ucenik nije pronadjen", HttpStatus.NOT_FOUND);
		}
	}

	// nadji roditelje cije se dete zove nekako
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/ucenik")
	public ResponseEntity<?> getRoditeljByImeUcenika(@RequestParam String imeUcenika) {
		Optional<Ucenik> ucenik = ucenikRepository.findByIme(imeUcenika);
		if (ucenik.isEmpty()) {
			logger.warn("Nije pronadjen ucenik sa imenom: {}", imeUcenika);
			return new ResponseEntity<>("Učenik nije pronađen", HttpStatus.NOT_FOUND);
		}

		Roditelj roditelj = ucenik.get().getRoditelj();
		if (roditelj == null) {
			return new ResponseEntity<>("Roditelj nije pronađen", HttpStatus.NOT_FOUND);
		}
		logger.info("Roditelj sa imenom ucenika: {} je uspesno pronadjen", imeUcenika);
		return new ResponseEntity<>(roditelj, HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
		
}
