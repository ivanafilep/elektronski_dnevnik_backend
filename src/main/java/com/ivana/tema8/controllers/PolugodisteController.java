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
import org.springframework.web.bind.annotation.RestController;

import com.ivana.tema8.dto.PolugodisteDTO;
import com.ivana.tema8.entities.Polugodiste;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PolugodisteRepository;
import com.ivana.tema8.services.FileHandlerServiceImpl;
import com.ivana.tema8.services.PolugodisteServiceImpl;

@RestController
@RequestMapping(path = "api/v1/polugodiste")
public class PolugodisteController {

	@Autowired
	private PolugodisteRepository polugodisteRepository;
	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private PolugodisteServiceImpl polugodisteService;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all polugodiste");
		return new ResponseEntity<Iterable<Polugodiste>>(polugodisteRepository.findAll(), HttpStatus.OK);
	}

	// CREATE POLUGODISTE
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST, path = "/novoPolugodiste")
	public ResponseEntity<?> addNewPolugodiste(@Valid @RequestBody PolugodisteDTO novoPolugodiste,
			BindingResult result) {
		return polugodisteService.addNewPolugodiste(novoPolugodiste, result);
	}
	
	//UPDATE
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updatePolugodiste(@PathVariable Integer id, @Valid @RequestBody PolugodisteDTO updatedPolugodiste, BindingResult result) {
		return polugodisteService.updatePolugodiste(id, updatedPolugodiste, result);
	}

	// DELETE POLUGODISTE
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deletePolugodiste(@PathVariable Integer id) {
		return polugodisteService.deletePolugodiste(id);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}


	
}
