package fr.esic.gestiondestock.services;

import java.math.BigDecimal;
import java.util.List;

import fr.esic.gestiondestock.dto.CommandeFournisseurDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.model.EtatCommande;

public interface CommandeFournisseurService {
	
	  CommandeFournisseurDto save(CommandeFournisseurDto dto);
	  
	  CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande);
	    
	  CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite);
	    
	  CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur);
	    
	  CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle);
	    
	    //Delete Article ==> delete LigneCommandeFournisseur
	  CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande);

	  CommandeFournisseurDto findById(Integer id);

	  CommandeFournisseurDto findByCode(String code);

	  List<CommandeFournisseurDto> findAll();
	  
	  List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande);

	  void delete(Integer id);

}
