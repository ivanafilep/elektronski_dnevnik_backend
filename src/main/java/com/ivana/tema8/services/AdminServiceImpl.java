
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
import com.ivana.tema8.entities.Admin;
import com.ivana.tema8.entities.RoleEntity;
import com.ivana.tema8.repositories.AdminRepository;
import com.ivana.tema8.repositories.RoleRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private RoleRepository roleRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		Admin newAdmin = new Admin();
		RoleEntity roleEntity = roleRepository.findById(1).orElse(null);

		newAdmin.setKorisnickoIme(newUser.getKorisnickoIme());
		newAdmin.setIme(newUser.getIme());
		newAdmin.setPrezime(newUser.getPrezime());
		newAdmin.setEmail(newUser.getEmail());

		if (newUser.getLozinka().equals(newUser.getPotvrdjenaLozinka())) {
			newAdmin.setLozinka(newUser.getLozinka());
		} else {
			return new ResponseEntity<>("Lozinke se ne poklapaju! Molimo unesite opet.", HttpStatus.BAD_REQUEST);
		}

		logger.info("Dodavanje novog admina.");
		newAdmin.setRole(roleEntity);

		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
			logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		adminRepository.save(newAdmin);
		logger.info("Novi admin uspešno dodat.");
		return new ResponseEntity<>(newAdmin, HttpStatus.CREATED);
	}

	
	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody KorisnikDTO updatedAdmin,
			BindingResult result) {
		logger.info("Pokušaj izmene admina sa id-jem {}", id);
		Admin admin = adminRepository.findById(id).get();

		admin.setKorisnickoIme(updatedAdmin.getKorisnickoIme());
		admin.setLozinka(updatedAdmin.getLozinka());
		admin.setIme(updatedAdmin.getIme());
		admin.setPrezime(updatedAdmin.getPrezime());
		admin.setEmail(updatedAdmin.getEmail());

		if (result.hasErrors()) {
			String errorMessage = createErrorMessage(result);
			logger.error("Validacija neuspela: {}", errorMessage);
			return new ResponseEntity<>(createErrorMessage(result), HttpStatus.BAD_REQUEST);
		}

		adminRepository.save(admin);
		logger.info("Admin sa id-jem {} je uspešno izmenjen", id);
		return new ResponseEntity<>(admin, HttpStatus.OK);
	}

	
	public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
		Optional<Admin> admin = adminRepository.findById(id);
		if (admin.isEmpty()) {
			logger.warn("Zahtev sa brisanje nastavnika sa nepostojecim ID {}", id);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			logger.info("DELETE zahtev za brisanje admina sa ID {}", id);
			adminRepository.delete(admin.get());
			return new ResponseEntity<>("Admin je uspesno obrisan", HttpStatus.OK);

		}
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}

}

