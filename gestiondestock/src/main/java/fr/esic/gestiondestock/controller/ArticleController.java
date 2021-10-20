package fr.esic.gestiondestock.controller;

import fr.esic.gestiondestock.controller.api.ArticleApi;
import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.dto.LigneCommandeClientDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.dto.LigneVenteDto;
import fr.esic.gestiondestock.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ArticleController implements ArticleApi {
    
	private ArticleService articleService;
	
    //Field Injection
    /*@Autowired
    private ArticleService articleService;

    //Getter Injection
    @Autowired
    public ArticleService getArticleService() {
        return articleService;
    } */

    //Constructor injection
    @Autowired
    public ArticleController(
            ArticleService articleService
    ) {
         this.articleService = articleService;
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        return articleService.save(dto);
    }

    @Override
    public ArticleDto findById(Integer idArticle) {
        return articleService.findById(idArticle);
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        return articleService.findByCodeArticle(codeArticle);
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleService.findAll();
    }

	@Override
	public List<LigneVenteDto> findHistoriqueVente(Integer idArticle) {
		return articleService.findHistoriqueVente(idArticle);
	}

	@Override
	public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle) {
		return articleService.findHistoriqueCommandeClient(idArticle);
	}

	@Override
	public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
		return articleService.findHistoriqueCommandeFournisseur(idArticle);
	}

	@Override
	public List<ArticleDto> findAllArticleByIdCategorie(Integer idCategory) {
		return articleService.findAllArticleByIdCategorie(idCategory);
	}
	
    @Override
    public void delete(Integer id) {
        articleService.delete(id);
    }
}
