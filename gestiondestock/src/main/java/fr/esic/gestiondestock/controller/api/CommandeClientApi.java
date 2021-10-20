package fr.esic.gestiondestock.controller.api;

import static fr.esic.gestiondestock.utils.Constants.APP_ROOT;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.esic.gestiondestock.dto.CommandeClientDto;
import fr.esic.gestiondestock.dto.LigneCommandeClientDto;
import fr.esic.gestiondestock.model.EtatCommande;
import io.swagger.annotations.Api;

@Api("commandesclients")
public interface CommandeClientApi {
	
	
	@PostMapping(APP_ROOT + "/commandesclients/create")
	ResponseEntity<CommandeClientDto> save(@RequestBody CommandeClientDto dto);
	
	@PatchMapping(APP_ROOT + "/commandesclients/update/etat/{idCommande}/{etatCommande}")
	ResponseEntity<CommandeClientDto> updateEtatCommande(
			@PathVariable("idCommande") Integer idCommande, 
			@PathVariable("etatCommande") EtatCommande etatCommande
			);
	
	@PatchMapping(APP_ROOT + "/commandesclients/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
	ResponseEntity<CommandeClientDto> updateQuantiteCommande(
			@PathVariable("idCommande") Integer idCommande, 
			@PathVariable("idLigneCommande") Integer idLigneCommande, 
			@PathVariable("quantite") BigDecimal quantite
			);
	
	@PatchMapping(APP_ROOT + "/commandesclients/update/client/{idCommande}/{idClient}")
	ResponseEntity<CommandeClientDto> updateClient(
			@PathVariable("idCommande") Integer idCommande, 
			@PathVariable("idClient") Integer idClient
			);
	
	@PatchMapping(APP_ROOT + "/commandesclients/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
	ResponseEntity<CommandeClientDto> updateArticle(
			@PathVariable("idCommande") Integer idCommande, 
			@PathVariable("idLigneCommande") Integer idLigneCommande, 
			@PathVariable("idArticle") Integer idArticle
			);
	
	@DeleteMapping(APP_ROOT + "/commandesclients/delete/article/{idCommande}/{idLigneCommande}")
	ResponseEntity<CommandeClientDto> deleteArticle(
			@PathVariable("idCommande") Integer idCommande, 
			@PathVariable("idLigneCommande") Integer idLigneCommande
	);
	
	@GetMapping(APP_ROOT + "/commandesclients/{idCommandeClient}")
	ResponseEntity<CommandeClientDto> findById(@PathVariable("idCommandeClient") Integer id);
	
	@GetMapping(APP_ROOT + "/commandesclients/{codeCommandeClient}")
	ResponseEntity<CommandeClientDto> findByCode(@PathVariable("codeCommandeClient") String code);
	
	@GetMapping(APP_ROOT + "/commandesclients/all")
	ResponseEntity<List<CommandeClientDto>> findAll();
	
	@GetMapping(APP_ROOT + "/commandesclients/lignesCommande/{idCommande}")
	ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(@PathVariable("idCommande") Integer idCommande);
	
	@DeleteMapping(APP_ROOT + "/commandesclients/delete/{idCommandeClient}")
	ResponseEntity<Void> delete(@PathVariable("idCommandeClient") Integer id);


}
