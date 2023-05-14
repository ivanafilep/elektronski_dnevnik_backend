/*
package com.ivana.tema8.security.config;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

// TODO OVO SVE JE DEKRIPCIJA TOKENA
public class JWTAuthorizationFilter extends OncePerRequestFilter {

	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer";

	private SecretKey secretKey;

	public JWTAuthorizationFilter(SecretKey secretKey) {
		super();
		this.secretKey = secretKey;
	}

	// TODO uvek isto, smemo da koristimo
	private boolean checkIfJWTTokenExists(HttpServletRequest request) {
		String authorizationHeaderValue = request.getHeader(HEADER);
		if (authorizationHeaderValue == null || !authorizationHeaderValue.startsWith(PREFIX)) {
			return false;
		}
		return true;
	}

	private Claims validateToken(HttpServletRequest request) {
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		return Jwts.parserBuilder().setSigningKey(this.secretKey).build().parseClaimsJws(jwtToken).getBody();
	}

	// TODO uvek isto, smemo da koristimo
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		List<String> authorities = (List<String>) claims.get("authorities");
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (checkIfJWTTokenExists(request)) {
			Claims claims = validateToken(request);
			if (claims.get("authorities") != null) {
				setUpSpringAuthentication(claims);
			} else {
				SecurityContextHolder.clearContext();
			}
		} else {
			SecurityContextHolder.clearContext();
		}
		filterChain.doFilter(request, response);

	}

}
*/

