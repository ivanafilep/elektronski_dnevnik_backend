package com.ivana.tema8.services;

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

import com.ivana.tema8.dto.PolugodisteDTO;
import com.ivana.tema8.entities.Polugodiste;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.PolugodisteRepository;

@Service
public class PolugodisteServiceImpl implements PolugodisteService {
	
	
	@Autowired
	private PolugodisteRepository polugodisteRepository;
	@Autowired 
	private OcenaRepository ocenaRepository;
	
	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	//CREATE 
	public ResponseEntity<?> addNewPolugodiste (@Valid @RequestBody PolugodisteDTO novoPolugodiste, BindingResult result) {
		Polugodiste newPolugodiste = new Polugodiste();
		
		newPolugodiste.setBrojPolugodista(novoPolugodiste.getBrojPolugodista());
		logger.info("Dodavanje novog polugodista.");
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		polugodisteRepository.save(newPolugodiste);
		logger.info("Novo polugodiste uspešno dodato.");
		return new ResponseEntity<>(newPolugodiste, HttpStatus.OK);
	}
	
	//UPDATE POL
	public ResponseEntity<?> updatePolugodiste(@PathVariable Integer id, @Valid @RequestBody PolugodisteDTO updatedPolugodiste, BindingResult result) {
		logger.info("Pokušaj izmene polugodista sa id-jem {}", id);
		Polugodiste polugodiste = polugodisteRepository.findById(id).get();
		
		polugodiste.setBrojPolugodista(updatedPolugodiste.getBrojPolugodista());
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		polugodisteRepository.save(polugodiste);
		logger.info("Polugodiste sa id-jem {} je uspešno izmenjen", id);
		return new ResponseEntity<>(polugodiste, HttpStatus.OK);
	}
	
	//DELETE
	public ResponseEntity<?> deletePolugodiste (@PathVariable Integer id) {
		Optional<Polugodiste> polugodiste = polugodisteRepository.findById(id);
			if (polugodiste.isEmpty()) {
				logger.warn("Zahtev sa brisanje polugodista sa nepostojecim ID {}", id);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				logger.info("DELETE zahtev za brisanje polugodista sa ID {}", id);
				polugodisteRepository.delete(polugodiste.get());
				return new ResponseEntity<>(HttpStatus.OK);
				
			}			
	}
	

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
	
}
