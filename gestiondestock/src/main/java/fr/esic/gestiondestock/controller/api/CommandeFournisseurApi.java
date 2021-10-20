package fr.esic.gestiondestock.controller.api;

import static fr.esic.gestiondestock.utils.Constants.COMMANDE_FOURNISSEUR_ENDPOINT;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.esic.gestiondestock.dto.CommandeFournisseurDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.model.EtatCommande;
import io.swagger.annotations.Api;

@Api("commandesfournisseurs")
public interface CommandeFournisseurApi {
	
	@PostMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/create")
	ResponseEntity<CommandeFournisseurDto> save(@RequestBody CommandeFournisseurDto dto);
	
	@PatchMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/update/etat/{idCommande}/{etatCommande}")
	CommandeFournisseurDto updateEtatCommande(@PathVariable("idCommande") Integer idCommande, @PathVariable("etatCommande") EtatCommande etatCommande);

	@PatchMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/update/quantite/{idCommande}/{idLigneCommande}/{quantite}")
	CommandeFournisseurDto updateQuantiteCommande(@PathVariable("idCommande") Integer idCommande,
	      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("quantite") BigDecimal quantite);

	@PatchMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/update/fournisseur/{idCommande}/{idFournisseur}")
	CommandeFournisseurDto updateFournisseur(@PathVariable("idCommande") Integer idCommande, @PathVariable("idFournisseur") Integer idFournisseur);

	@PatchMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/update/article/{idCommande}/{idLigneCommande}/{idArticle}")
	CommandeFournisseurDto updateArticle(@PathVariable("idCommande") Integer idCommande,
	      @PathVariable("idLigneCommande") Integer idLigneCommande, @PathVariable("idArticle") Integer idArticle);

	@DeleteMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/delete/article/{idCommande}/{idLigneCommande}")
	CommandeFournisseurDto deleteArticle(@PathVariable("idCommande") Integer idCommande, @PathVariable("idLigneCommande") Integer idLigneCommande);
	
	@GetMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/{idCommandeFournisseur}")
	CommandeFournisseurDto findById(@PathVariable("idCommandeFournisseur") Integer id);
	
	@GetMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/{codeCommandeFournisseur}")
	CommandeFournisseurDto findByCode(@PathVariable("codeCommandeFournisseur") String code);
	
	@GetMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/all")
	List<CommandeFournisseurDto> findAll();
	
	@GetMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/lignesCommande/{idCommande}")
	ResponseEntity<List<LigneCommandeFournisseurDto>> findAllLignesCommandesFournisseurByCommandeFournisseurId(@PathVariable("idCommande") Integer idCommande);
	
	@DeleteMapping(COMMANDE_FOURNISSEUR_ENDPOINT + "/delete/{idCommandeFournisseur}")
	void delete(@PathVariable("idCommandeFournisseur") Integer id);

}
