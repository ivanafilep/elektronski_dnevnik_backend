
package com.ivana.tema8.controllers;

import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
//import java.net.http.HttpHeaders;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ivana.tema8.services.FileHandlerServiceImpl;

import io.jsonwebtoken.io.IOException;

@RestController
@RequestMapping("/download")
public class DownloadController {

	private final Logger logger = LoggerFactory.getLogger(FileHandlerServiceImpl.class);

	@GetMapping("/log")
	@Secured("ROLE_ADMIN")
	public ResponseEntity<ByteArrayResource> downloadLogFile(Authentication authentication) throws java.io.IOException {

		File files = new File("logs/spring-boot-logging.log");
		Path path = Paths.get(files.getAbsolutePath());
		
		if (!files.exists()) {
			logger.error("Ne postoji file.");
			return ResponseEntity.notFound().build();
		}

		ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(path));

	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename= " + files.getName());

	    return ResponseEntity.ok()
	            .headers(headers)
	            .body(resource);
	}
	
}

