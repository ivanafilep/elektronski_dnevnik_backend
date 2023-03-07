package com.ivana.tema8.controllers;





import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//import com.ivana.tema8.dto.UserDTO;
import com.ivana.tema8.entities.Korisnik;
import com.ivana.tema8.repositories.KorisnikRepository;
//import com.ivana.tema8.util.Encryption;
import com.ivana.tema8.services.FileHandlerServiceImpl;

//import io.jsonwebtoken.Jwts;

@RestController
public class KorisnikController {
	
	@Autowired
	private KorisnikRepository korisnikRepository;
	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);
	//ADMIN RADI OVO
//	
//	@Autowired
//	private SecretKey secretKey;
//	
//	@Value("${spring.security.token-duration}")
//	private Integer tokenDuration;
	
	@RequestMapping(method = RequestMethod.GET, path = "/api/v1/korisnik")
	public ResponseEntity<?> getAll() {
		logger.info("Getting all korisnici.");
		return new ResponseEntity<Iterable<Korisnik>>(korisnikRepository.findAll(), HttpStatus.OK);
	}
	
//	private String getJWTToken(Korisnik korisnikEntity) {
//		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
//				.commaSeparatedStringToAuthorityList(korisnikEntity.getRole().getIme());
//		
//		String token = Jwts.builder().setId("softtekJWT").setSubject(korisnikEntity.getEmail())
//				.claim("authorities",
//				grantedAuthorities.stream().map(GrantedAuthority ::getAuthority).collect(Collectors.toList()))
//				.setIssuedAt(new Date(System.currentTimeMillis()))
//				.setExpiration(new Date(System.currentTimeMillis() + this.tokenDuration))
//				.signWith(this.secretKey).compact();
//		
//		return "Bearer " + token;
//	}
//	
//	@RequestMapping(method = RequestMethod.POST, path = "api/v1/login")
//	public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
//		Korisnik korisnik = korisnikRepository.findByEmail(email);
//		if(korisnik != null && Encryption.validatePassword(password, korisnik.getLozinka())) {
//			
//			String token = getJWTToken(korisnik);
//			UserDTO retVal = new UserDTO(email, token);
//			return new ResponseEntity<UserDTO>(retVal, HttpStatus.OK);
//			
//		}
//		return new ResponseEntity<>("Wrong credentials", HttpStatus.UNAUTHORIZED);
//	}
}
