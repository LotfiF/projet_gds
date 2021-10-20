package fr.esic.gestiondestock.controller.api;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.esic.gestiondestock.dto.CategoryDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static fr.esic.gestiondestock.utils.Constants.APP_ROOT;

@Api("categories")
public interface CategoryApi {
	
    @PostMapping(value = APP_ROOT + "/categories/create", 
    		consumes = MediaType.APPLICATION_JSON_VALUE,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Enregistrer une catégorie", 
    	notes = "Cette méthode permet d'enregistrer ou de modifier une catégorie", 
    	response = CategoryDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="L'objet category a été crée/modifié avec succès."),
    		@ApiResponse(code = 400, message="L'objet category n'est pas valide")
    })
    CategoryDto save(@RequestBody CategoryDto dto);
    
    
    @GetMapping(value = APP_ROOT + "/categories/{idCategory}", 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Recherche une categorie par ID", 
		notes = "Cette méthode permet de chercher et récupérer une categorie par son ID", 
		response = CategoryDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="la catégorie a été trouvé dans la BDD"),
    		@ApiResponse(code = 404, message="Aucune catégorie n'existe dans la BDD avec l'ID fourni")
    })
    CategoryDto findById(@PathVariable("idCategory") Integer idCategory);
    
    
    @GetMapping(value = APP_ROOT + "/categories/filter/{codeCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Recherche un catégorie par code", 
		notes = "Cette méthode permet de chercher et récupérer une catégorie par son CODE", 
		response = CategoryDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="la catégorie a été trouvé dans la BDD"),
    		@ApiResponse(code = 404, message="Aucune catégorie n'existe dans la BDD avec le CODE fourni")
    })
    CategoryDto findByCode(@PathVariable("codeCategory") String codeCategory);
    
    @GetMapping(value = APP_ROOT + "/categories/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Récupérer la liste des catégories", 
		notes = "Cette méthode permet de renvoyer la liste des catégories dans la BDD", 
		responseContainer = "List<CategoryDto>")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="La liste des categories (voir une liste vide).")
    })
    List<CategoryDto> findAll();
    
    @DeleteMapping(value = APP_ROOT + "/categories/delete/{idCategory}")
    @ApiOperation(value="Supprimer une catégorie", 
		notes = "Cette méthode permet de supprimer une catégorie identifié par son ID.", 
		response = CategoryDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="La catégorie a été supprimé.")
    })
    void delete(@PathVariable("idCategory")Integer id);

}
