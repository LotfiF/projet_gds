package fr.esic.gestiondestock.services;

import java.util.List;

import fr.esic.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import fr.esic.gestiondestock.dto.UtilisateurDto;

public interface UtilisateurService {
	
	UtilisateurDto save(UtilisateurDto dto);
	
	UtilisateurDto findById(Integer id);
	
	List<UtilisateurDto> findAll();
	
	UtilisateurDto findByMail(String mail);
	
	void delete(Integer id);
	
	UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto);

}
