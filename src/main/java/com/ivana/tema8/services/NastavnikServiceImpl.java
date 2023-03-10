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

import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.Nastavnik;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.NastavnikRepository;
import com.ivana.tema8.repositories.PredmetRepository;
import com.ivana.tema8.repositories.RoleRepository;

@Service
public class NastavnikServiceImpl implements NastavnikPredmetService {
	
	@Autowired
	private NastavnikRepository nastavnikRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PredmetRepository predmetRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;
	
	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);
	
	
	public ResponseEntity<?> addNewNastavnik (@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		Nastavnik newNastavnik = new Nastavnik();
		RoleEntity roleEntity = roleRepository.findById(3).orElse(null);

		newNastavnik.setKorisnickoIme(newUser.getKorisnickoIme());
		newNastavnik.setIme(newUser.getIme());
		newNastavnik.setPrezime(newUser.getPrezime());
		newNastavnik.setEmail(newUser.getEmail());
		
		if (newUser.getLozinka().equals(newUser.getPotvrdjenaLozinka())) {
			newNastavnik.setLozinka(newUser.getLozinka());
		} else {
			return new ResponseEntity<>("Lozinke se ne poklapaju! Molimo unesite opet.", HttpStatus.BAD_REQUEST);
		}

		
		
		logger.info("Dodavanje novog nastavnika.");
		newNastavnik.setRole(roleEntity);
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage); new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		nastavnikRepository.save(newNastavnik);
		logger.info("Novi nastavnik uspešno dodat.");
		return new ResponseEntity<>(newNastavnik, HttpStatus.CREATED);
	}
	
	
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedNastavnik, BindingResult result) {
		logger.info("Pokušaj izmene nastavnika sa id-jem {}", id);
		Nastavnik nastavnik = nastavnikRepository.findById(id).get();
		
		nastavnik.setKorisnickoIme(updatedNastavnik.getKorisnickoIme());
		nastavnik.setLozinka(updatedNastavnik.getLozinka());
		nastavnik.setIme(updatedNastavnik.getIme());
		nastavnik.setPrezime(updatedNastavnik.getPrezime());
		nastavnik.setEmail(updatedNastavnik.getEmail());
		
		
		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
	        logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
        }
		
		nastavnikRepository.save(nastavnik);
		logger.info("Nastavnik sa id-jem {} je uspešno izmenjen", id);
		return new ResponseEntity<>(nastavnik, HttpStatus.OK);
	}
	
	
	public ResponseEntity<?> deleteNastavnik (@PathVariable Integer id) {
	    Optional<Nastavnik> nastavnik = nastavnikRepository.findById(id);
	    if (nastavnik.isEmpty()) {
	        logger.warn("Zahtev sa brisanje nastavnika sa nepostojecim ID {}", id);
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    } else {
	        logger.info("DELETE zahtev za brisanje nastavnika sa ID {}", id);
	        List<NastavnikPredmet> nastavnikPredmeti = nastavnikPredmetRepository.findByNastavnikId(id);
	        for (NastavnikPredmet np : nastavnikPredmeti) {
	            np.setNastavnik(null); // postavljanje reference na null
	            nastavnikPredmetRepository.save(np); // čuvanje izmena
	        }
	        nastavnikRepository.deleteById(id); // brisanje nastavnika
	        return new ResponseEntity<>("Nastavnik je uspesno obrisan.", HttpStatus.OK);
	    }			
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
	

}
