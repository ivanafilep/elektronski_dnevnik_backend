package com.ivana.tema8.controllers;

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
import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.Korisnik;
import com.ivana.tema8.entities.Roditelj;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.RoditeljRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.repositories.UcenikRepository;
import com.ivana.tema8.services.FileHandlerServiceImpl;

@RestController
@RequestMapping(path = "api/v1/roditelj")
public class RoditeljController {
	
	@Autowired
	private RoditeljRepository roditeljRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private UcenikRepository ucenikRepository;
	
	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all roditelji");
		return new ResponseEntity<Iterable<Roditelj>>(roditeljRepository.findAll(), HttpStatus.OK);
	}
	
	//ADMIN MOZE OVO
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewRoditelj (@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		Roditelj newRoditelj = new Roditelj();
		RoleEntity roleEntity = roleRepository.findById(4).orElse(null);

		newRoditelj.setKorisnickoIme(newUser.getKorisnickoIme());
		newRoditelj.setLozinka(newUser.getLozinka());
		newRoditelj.setIme(newUser.getIme());
		newRoditelj.setPrezime(newUser.getPrezime());
		newRoditelj.setEmail(newUser.getEmail());
		newRoditelj.setPotvrdjenaLozinka(newUser.getPotvrdjenaLozinka());
		
		logger.info("Dodavanje novog roditelja.");
		newRoditelj.setRole(roleEntity);
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		roditeljRepository.save(newRoditelj);
		logger.info("Novi roditelj uspešno dodat.");
		return new ResponseEntity<>(newRoditelj, HttpStatus.CREATED);
	}
	
	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
	
	//ADMIN MOZE OVO
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteRoditelj (@PathVariable Integer id) {
		Optional <Roditelj> roditelj = roditeljRepository.findById(id);
			if (roditelj.isEmpty()) {
				logger.warn("Zahtev sa brisanje roditelja sa nepostojecim ID {}", id);
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			} else {
				logger.info("DELETE zahtev za brisanje roditelja sa ID {}", id);
				roditeljRepository.delete(roditelj.get());
				return new ResponseEntity<>(HttpStatus.OK);
				
			}						
	}
	
	//ADMIN MOZE OVO
		@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
		public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedRoditelj, BindingResult result) {
			logger.info("Pokušaj izmene roditelja sa id-jem {}", id);
			Roditelj roditelj = roditeljRepository.findById(id).get();
			
			roditelj.setKorisnickoIme(updatedRoditelj.getKorisnickoIme());
			roditelj.setLozinka(updatedRoditelj.getLozinka());
			roditelj.setIme(updatedRoditelj.getIme());
			roditelj.setPrezime(updatedRoditelj.getPrezime());
			roditelj.setEmail(updatedRoditelj.getEmail());
			roditelj.setPotvrdjenaLozinka(updatedRoditelj.getPotvrdjenaLozinka());
			
			if (result.hasErrors()) {
				String errorMessage = createErrorMessage(result);
		        logger.error("Validacija neuspela: {}", errorMessage);
				return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
	        }
			
			roditeljRepository.save(roditelj);
			logger.info("Roditelj sa id-jem {} je uspešno izmenjen", id);
			return new ResponseEntity<>(roditelj, HttpStatus.OK);
		}
		
		@RequestMapping(method = RequestMethod.GET, path = "/{id}")
		public ResponseEntity<?> getRoditeljkById (@PathVariable Integer id) {
			Optional<Roditelj> roditelj = roditeljRepository.findById(id);
			
			if (!roditelj.isPresent()) {
				logger.warn("Nije pronadjen roditelj sa ID: {}", id);
		        return new ResponseEntity<>("Roditelj nije pronadjen", HttpStatus.NOT_FOUND);
		    }
			logger.info("Roditelj sa ID: {} je uspesno pronadjen", id);
		    return new ResponseEntity<>(roditelj.get(), HttpStatus.OK);
		}
		
		@RequestMapping(method = RequestMethod.GET, path = "/by-name")
		public ResponseEntity<?> getRoditeljByName (@RequestParam String ime) {
			Optional<Roditelj> roditelj = roditeljRepository.findByIme(ime);
			
			if (!roditelj.isPresent()) {
				logger.warn("Nije pronadjen roditelj sa imenom: {}", ime);
		        return new ResponseEntity<>("Roditelj nije pronadjen", HttpStatus.NOT_FOUND);
		    }
			logger.info("Roditelj sa imenom: {} je uspesno pronadjen", ime);
		    return new ResponseEntity<>(roditelj.get(), HttpStatus.OK);
		}
		
		//nadji roditelja po id-u ucenika
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

		//nadji roditelje cije se dete zove nekako
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

		
}
