package fr.esic.gestiondestock.services;

import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.dto.LigneCommandeClientDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.dto.LigneVenteDto;

import java.util.List;

public interface ArticleService {

    ArticleDto save(ArticleDto dto);

    ArticleDto findById(Integer id);

    ArticleDto findByCodeArticle(String codeArticle);

    List<ArticleDto> findAll();
    
    List<LigneVenteDto> findHistoriqueVente(Integer idArticle);
    
    List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle);
    
    List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle);
    
    List<ArticleDto> findAllArticleByIdCategorie(Integer idCategory);

    void delete(Integer id);
}
