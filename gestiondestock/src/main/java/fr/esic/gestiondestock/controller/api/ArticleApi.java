package fr.esic.gestiondestock.controller.api;

import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.dto.LigneCommandeClientDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.dto.LigneVenteDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.ApiResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static  fr.esic.gestiondestock.utils.Constants.APP_ROOT;

@Api("articles")
public interface ArticleApi {

    @PostMapping(value = APP_ROOT + "/articles/create", 
    		consumes = MediaType.APPLICATION_JSON_VALUE,
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Enregistrer un article", 
    	notes = "Cette méthode permet d'enregistrer ou de modifier un article", 
    	response = ArticleDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="L'objet article a été créé/modifié avec succès."),
    		@ApiResponse(code = 400, message="L'objet article n'est pas valide")
    })
    ArticleDto save(@RequestBody ArticleDto dto);

    
    @GetMapping(value = APP_ROOT + "/articles/{idArticle}", 
    		produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Recherche un article par ID", 
		notes = "Cette méthode permet de chercher et récupérer un article par son ID", 
		response = ArticleDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="l'article a été trouvé dans la BDD"),
    		@ApiResponse(code = 404, message="Aucun article n'existe dans la BDD avec l'ID fourni")
    })
    ArticleDto findById(@PathVariable("idArticle") Integer idArticle);

    
    @GetMapping(value = APP_ROOT + "/articles/filter/{codeArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Recherche un article par code", 
		notes = "Cette méthode permet de chercher et récupérer un article par son CODE_ARTICLE", 
		response = ArticleDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="l'article a été trouvé dans la BDD"),
    		@ApiResponse(code = 404, message="Aucun article n'existe dans la BDD avec le CODE fourni")
    })
    ArticleDto findByCodeArticle(@PathVariable("codeArticle") String codeArticle);

    
    @GetMapping(value = APP_ROOT + "/articles/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value="Récupérer la liste des articles", 
		notes = "Cette méthode permet de renvoyer la liste des articles dans la BDD", 
		responseContainer = "List<ArticleDto>")
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="La liste des articles (voir une liste vide).")
    })
    List<ArticleDto> findAll();
    
    
    @GetMapping(value = APP_ROOT + "/articles/historique/vente/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneVenteDto> findHistoriqueVente(@PathVariable("idArticle") Integer idArticle);
    
    @GetMapping(value = APP_ROOT + "/articles/historique/commandeclient/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(@PathVariable("idArticle") Integer idArticle);
    
    @GetMapping(value = APP_ROOT + "/articles/historique/commandefournisseur/{idArticle}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(@PathVariable("idArticle") Integer idArticle);
    
    @GetMapping(value = APP_ROOT + "/articles/filter/category/{idCategory}", produces = MediaType.APPLICATION_JSON_VALUE)
    List<ArticleDto> findAllArticleByIdCategorie(@PathVariable("idCategory") Integer idCategory);
    

    @DeleteMapping(value = APP_ROOT + "/articles/delete/{idArticle}")
    @ApiOperation(value="Supprimer un article", 
		notes = "Cette méthode permet de supprimer un article identifié par son ID.", 
		response = ArticleDto.class)
    @ApiResponses(value = {
    		@ApiResponse(code = 200, message="L'article a été supprimé.")
    })
    void delete(@PathVariable("idArticle") Integer id);
}
