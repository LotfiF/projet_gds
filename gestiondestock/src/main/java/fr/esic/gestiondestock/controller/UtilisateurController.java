package fr.esic.gestiondestock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.gestiondestock.controller.api.UtilisateurApi;
import fr.esic.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import fr.esic.gestiondestock.dto.UtilisateurDto;
import fr.esic.gestiondestock.services.UtilisateurService;

@RestController
public class UtilisateurController implements UtilisateurApi {
	
	private UtilisateurService utilisateurService;

	@Autowired
	public UtilisateurController(UtilisateurService utilisateurService) {
		this.utilisateurService = utilisateurService;
	}	

	@Override
	public UtilisateurDto save(UtilisateurDto dto) {
		return utilisateurService.save(dto);
	}
	
	@Override
	  public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
	    return utilisateurService.changerMotDePasse(dto);
	}


	@Override
	public UtilisateurDto findById(Integer id) {
		 return utilisateurService.findById(id);
	}
	
	@Override
	  public UtilisateurDto findByMail(String mail) {
	    return utilisateurService.findByMail(mail);
	}



	@Override
	public List<UtilisateurDto> findAll() {
		 return utilisateurService.findAll();
	}

	@Override
	public void delete(Integer id) {
		 utilisateurService.delete(id);
	}

}
