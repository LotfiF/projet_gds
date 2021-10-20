package fr.esic.gestiondestock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.gestiondestock.controller.api.AuthenticationApi;
import fr.esic.gestiondestock.dto.auth.AuthenticationRequest;
import fr.esic.gestiondestock.dto.auth.AuthenticationResponse;
import fr.esic.gestiondestock.model.auth.ExtendedUser;
import fr.esic.gestiondestock.services.auth.ApplicationUserDetailsService;
import fr.esic.gestiondestock.utils.JwtUtil;

@RestController
public class AuthenticationController implements AuthenticationApi {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ApplicationUserDetailsService applicationUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request){
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(
						request.getLogin(),
						request.getPassword()
				)
		);
		final UserDetails userDetails = applicationUserDetailsService.loadUserByUsername(request.getLogin());
		
		final String jwt = jwtUtil.generateToken((ExtendedUser) userDetails);
		
		return ResponseEntity
				.ok(AuthenticationResponse
						.builder()
						.accessToken(jwt)
						.build()
		);
	}

}
