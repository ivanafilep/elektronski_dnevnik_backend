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
import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.Roditelj;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.RoditeljRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.repositories.UcenikRepository;

@Service
public class RoditeljServiceImpl implements RoditeljService {

	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UcenikRepository ucenikRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	// REGISTRACIJA RODITELJA
	public ResponseEntity<?> addNewRoditelj(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		Roditelj newRoditelj = new Roditelj();
		RoleEntity roleEntity = roleRepository.findById(4).orElse(null);

		newRoditelj.setKorisnickoIme(newUser.getKorisnickoIme());
		newRoditelj.setIme(newUser.getIme());
		newRoditelj.setPrezime(newUser.getPrezime());
		newRoditelj.setEmail(newUser.getEmail());

		if (newUser.getLozinka().equals(newUser.getPotvrdjenaLozinka())) {
			newRoditelj.setLozinka(newUser.getLozinka());
		} else {
			return new ResponseEntity<>("Lozinke se ne poklapaju! Molimo unesite opet.", HttpStatus.BAD_REQUEST);
		}

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

	// UPDATE RODITELJA
	public ResponseEntity<?> updateRoditelj(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedRoditelj,
			BindingResult result) {
		logger.info("Pokušaj izmene roditelja sa id-jem {}", id);
		Roditelj roditelj = roditeljRepository.findById(id).get();

		roditelj.setKorisnickoIme(updatedRoditelj.getKorisnickoIme());
		roditelj.setLozinka(updatedRoditelj.getLozinka());
		roditelj.setIme(updatedRoditelj.getIme());
		roditelj.setPrezime(updatedRoditelj.getPrezime());
		roditelj.setEmail(updatedRoditelj.getEmail());

		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
			logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		roditeljRepository.save(roditelj);
		logger.info("Roditelj sa id-jem {} je uspešno izmenjen", id);
		return new ResponseEntity<>(roditelj, HttpStatus.OK);
	}

	// BRISANJE RODITELJA
	public ResponseEntity<?> deleteRoditelj(@PathVariable Integer id) {
		Optional<Roditelj> roditelj = roditeljRepository.findById(id);
		if (roditelj.isEmpty()) {
			logger.warn("Zahtev sa brisanje roditelja sa nepostojecim ID {}", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {

			if (!roditelj.get().getDete().isEmpty()) {
				for (Ucenik ucenik : roditelj.get().getDete()) {
					ucenik.setRoditelj(null);
					ucenikRepository.save(ucenik);
				}
			}

			logger.info("DELETE zahtev za brisanje roditelja sa ID {}", id);
			roditeljRepository.delete(roditelj.get());
			return new ResponseEntity<>("Roditelj je uspesno obrisan.", HttpStatus.OK);

		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}
}
