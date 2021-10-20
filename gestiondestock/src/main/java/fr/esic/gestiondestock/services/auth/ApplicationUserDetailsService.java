package fr.esic.gestiondestock.services.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.esic.gestiondestock.dto.UtilisateurDto;
import fr.esic.gestiondestock.model.auth.ExtendedUser;
import fr.esic.gestiondestock.services.UtilisateurService;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UtilisateurService utilisateurService;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UtilisateurDto utilisateur = utilisateurService.findByMail(email);
	    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
	    utilisateur.getRoles().forEach(role -> 
	    	authorities.add(new SimpleGrantedAuthority(role.getRoleName())
	    ));

	    return new ExtendedUser(
	    		utilisateur.getMail(), 
	    		utilisateur.getMotDePasse(), 
	    		utilisateur.getEntreprise().getId(), 
	    		authorities
	    );

	}

}
