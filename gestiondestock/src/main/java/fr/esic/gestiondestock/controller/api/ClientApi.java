package fr.esic.gestiondestock.controller.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.esic.gestiondestock.dto.ClientDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static  fr.esic.gestiondestock.utils.Constants.APP_ROOT;

import java.util.List;

@Api("clients")
public interface ClientApi {
	
	  @PostMapping(value = APP_ROOT + "/clients/create", 
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	  @ApiOperation(value="Enregistrer un client", 
	  		notes = "Cette méthode permet d'enregistrer ou de modifier un client", 
	  		response = ClientDto.class)
	  @ApiResponses(value = {
		@ApiResponse(code = 200, message="L'objet client a été créé/modifié avec succès."),
		@ApiResponse(code = 400, message="L'objet client n'est pas valide")
	  })
	  ClientDto save(@RequestBody ClientDto dto);

	  @GetMapping(value = APP_ROOT + "/clients/{idClient}", produces = MediaType.APPLICATION_JSON_VALUE)
	  @ApiOperation(value="Recherche un client par ID", 
			notes = "Cette méthode permet de chercher et récupérer un client par son ID", 
			response = ClientDto.class)
	  @ApiResponses(value = {
	    	@ApiResponse(code = 200, message="le client a été trouvé dans la BDD"),
	    	@ApiResponse(code = 404, message="Aucun client n'existe dans la BDD avec l'ID fourni")
	   })
	  ClientDto findById(@PathVariable("idClient") Integer id);

	  @GetMapping(value = APP_ROOT + "/clients/all", produces = MediaType.APPLICATION_JSON_VALUE)
	  @ApiOperation(value="Récupérer la liste des clients", 
			notes = "Cette méthode permet de renvoyer la liste des clients dans la BDD", 
			responseContainer = "List<ClientDto>")
	  @ApiResponses(value = {
	    	@ApiResponse(code = 200, message="La liste des clients (voir une liste vide).")
	  })
	  List<ClientDto> findAll();

	  @DeleteMapping(value = APP_ROOT + "/clients/delete/{idClient}")
	  @ApiOperation(value="Supprimer un client", 
			notes = "Cette méthode permet de supprimer un client identifié par son ID.", 
			response = ClientDto.class)
	  @ApiResponses(value = {
	    	@ApiResponse(code = 200, message="Le client a été supprimé.")
	  })
	  void delete(@PathVariable("idClient") Integer id);

}
