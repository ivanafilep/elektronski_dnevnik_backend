
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
import com.ivana.tema8.entities.Admin;
import com.ivana.tema8.repositories.AdminRepository;
import com.ivana.tema8.repositories.RoleRepository;
import com.ivana.tema8.services.AdminServiceImpl;
import com.ivana.tema8.services.FileHandlerServiceImpl;

@RestController
@RequestMapping(path = "api/v1/admin")
public class AdminController {

	@Autowired
	private AdminRepository adminRepository;
	@Autowired
	private AdminServiceImpl adminService;
	@Autowired
	private RoleRepository roleRepository;

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> getAll() {
		logger.info("Getting all admins.");
		return new ResponseEntity<Iterable<Admin>>(adminRepository.findAll(), HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> addNewAdmin(@Valid @RequestBody KorisnikDTO newUser, BindingResult result) {
		return adminService.addNewAdmin(newUser, result);
	}

	
	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.PUT, path = "/{id}")
	public ResponseEntity<?> updateAdmin(@PathVariable Integer id, @RequestBody KorisnikDTO updatedAdmin,
			BindingResult result) {
		return adminService.updateAdmin(id, updatedAdmin, result);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
	public ResponseEntity<?> deleteAdmin(@PathVariable Integer id) {
		return adminService.deleteAdmin(id);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/{id}")
	public ResponseEntity<?> getAdminById(@PathVariable Integer id) {
		Optional<Admin> admin = adminRepository.findById(id);

		if (!admin.isPresent()) {
			logger.warn("Nije pronadjen admin sa ID: {}", id);
			return new ResponseEntity<>("Admin nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Admin sa ID: {} je uspesno pronadjen", id);
		return new ResponseEntity<>(admin.get(), HttpStatus.OK);
	}

	
	@Secured("ROLE_ADMIN")
	@RequestMapping(method = RequestMethod.GET, path = "/by-name")
	public ResponseEntity<?> getAdminByIme(@RequestParam String ime) {
		Optional<Admin> admin = adminRepository.findByIme(ime);

		if (!admin.isPresent()) {
			logger.warn("Nije pronadjen admin sa imenom: {}", ime);
			return new ResponseEntity<>("Admin nije pronadjen", HttpStatus.NOT_FOUND);
		}
		logger.info("Admin sa imenom: {} je uspesno pronadjen", ime);
		return new ResponseEntity<>(admin.get(), HttpStatus.OK);
	}

	private String createErrorMessage(BindingResult result) {
		return result.getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.joining("\n"));

	}

	
}

