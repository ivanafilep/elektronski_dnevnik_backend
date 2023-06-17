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
import com.ivana.tema8.dto.KorisnikDTO;
import com.ivana.tema8.entities.NastavnikPredmet;
import com.ivana.tema8.entities.Roditelj;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.entities.Ucenik;
import com.ivana.tema8.repositories.NastavnikPredmetRepository;
import com.ivana.tema8.repositories.OcenaRepository;
import com.ivana.tema8.repositories.RoditeljRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.repositories.UcenikRepository;

@Service
public class UcenikServiceImpl implements UcenikService {

	@Autowired
	private UcenikRepository ucenikRepository;
	@Autowired
	private OcenaRepository ocenaRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private RoditeljRepository roditeljRepository;
	@Autowired
	private NastavnikPredmetRepository nastavnikPredmetRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	public void obrisiOceneUcenika(Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isPresent()) {
			ucenik.get().getOcene().forEach(ocena -> ocenaRepository.delete(ocena));
		}
	}

	public void obrisiUcenika(Integer id) {
		Optional<Ucenik> ucenik = ucenikRepository.findById(id);
		if (ucenik.isPresent()) {
			ucenikRepository.delete(ucenik.get());
		}
	}

	
	public ResponseEntity<?> addNewUcenik(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		Ucenik newUcenik = new Ucenik();
		RoleEntity roleEntity = roleRepository.findById(2).orElse(null);

		newUcenik.setKorisnickoIme(newUser.getKorisnickoIme());
		newUcenik.setIme(newUser.getIme());
		newUcenik.setPrezime(newUser.getPrezime());
		newUcenik.setEmail(newUser.getEmail());

		if (newUser.getPotvrdjenaLozinka() != null && newUser.getPotvrdjenaLozinka().equals(newUser.getLozinka())) {
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

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}

	
	public ResponseEntity<?> updateUcenik(@PathVariable Integer id, @Valid @RequestBody KorisnikDTO updatedUcenik,
			BindingResult result) {
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

	
	public ResponseEntity<?> dodeliRoditeljaUceniku(@PathVariable Integer ucenikId, @PathVariable Integer roditeljId) {

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

	
	public ResponseEntity<?> dodeliNastavnikPredmetUceniku(@PathVariable Integer ucenikId,
			@PathVariable Integer nastavnikPredmetId) {
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

}
