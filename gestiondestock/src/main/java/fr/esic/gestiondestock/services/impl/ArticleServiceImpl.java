package fr.esic.gestiondestock.services.impl;

import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.dto.LigneCommandeClientDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.dto.LigneVenteDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Article;
import fr.esic.gestiondestock.model.LigneCommandeClient;
import fr.esic.gestiondestock.model.LigneCommandeFournisseur;
import fr.esic.gestiondestock.model.LigneVente;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.LigneCommandeClientRepository;
import fr.esic.gestiondestock.repository.LigneCommandeFournisseurRepository;
import fr.esic.gestiondestock.repository.LigneVenteRepository;
import fr.esic.gestiondestock.services.ArticleService;
import fr.esic.gestiondestock.validator.ArticleValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;
    private LigneVenteRepository ligneVenteRepository;
    private LigneCommandeClientRepository ligneCommandeClientRepository;
    private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, 
    		LigneVenteRepository ligneVenteRepository, 
    		LigneCommandeClientRepository ligneCommandeClientRepository, 
    		LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository) {
        this.articleRepository = articleRepository;
        this.ligneVenteRepository = ligneVenteRepository;
        this.ligneCommandeClientRepository = ligneCommandeClientRepository;
        this.ligneCommandeFournisseurRepository =ligneCommandeFournisseurRepository;
        
    }

    @Override
    public ArticleDto save(ArticleDto dto) {
        List<String> errors = ArticleValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Article is not valid {}", dto);
            throw new InvalidEntityException("l'article n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }
        return ArticleDto.fromEntity(
                articleRepository.save(
                        ArticleDto.toEntity(dto)
                )
        );
    }

    @Override
    public ArticleDto findById(Integer id) {
        if (id == null){
            log.error("article Id is null");
            return null;
        }
        return articleRepository.findById(id)
        		.map(ArticleDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
                        "Aucun article avec l'ID = " + id + "n'a été trouvé",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        				);
    }

    @Override
    public ArticleDto findByCodeArticle(String codeArticle) {
        if (!StringUtils.hasLength(codeArticle)){
            log.error("le code article is null");
            return null;
        }
        return articleRepository.findArticleByCodeArticle(codeArticle)
        		 .map(ArticleDto::fromEntity)
        		 .orElseThrow(() ->
        		 	new EntityNotFoundException(
                        "Aucun article avec le code = " + codeArticle + " n'a été trouvé",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        			);
    }

    @Override
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(ArticleDto::fromEntity)
                .collect(Collectors.toList());
    }

	@Override
	public List<LigneVenteDto> findHistoriqueVente(Integer idArticle) {
		return ligneVenteRepository.findAllByArticleId(idArticle).stream()
				.map(LigneVenteDto::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<LigneCommandeClientDto> findHistoriqueCommandeClient(Integer idArticle) {
		return ligneCommandeClientRepository.findAllByArticleId(idArticle).stream()
				.map(LigneCommandeClientDto::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<LigneCommandeFournisseurDto> findHistoriqueCommandeFournisseur(Integer idArticle) {
		return ligneCommandeFournisseurRepository.findAllByArticleId(idArticle).stream()
				.map(LigneCommandeFournisseurDto::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<ArticleDto> findAllArticleByIdCategorie(Integer idCategory) {
		return articleRepository.findAllByCategoryId(idCategory).stream()
			.map(ArticleDto::fromEntity)
			.collect(Collectors.toList());
	}
	
	
    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("article Id is null");
            return;
        }
        List<LigneCommandeClient> ligneCommandeClient = ligneCommandeClientRepository.findAllByArticleId(id);
        if (!ligneCommandeClient.isEmpty()) {
        	throw new InvalidOperationException("impossible de supprimer un article déja utilisé dans des commmandes clients", 
        			ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseur = ligneCommandeFournisseurRepository.findAllByArticleId(id);
        if (!ligneCommandeFournisseur.isEmpty()) {
        	throw new InvalidOperationException("impossible de supprimer un article déja utilisé dans des commmandes fournisseurs", 
        			ErrorCodes.ARTICLE_ALREADY_IN_USE);
        }
        List<LigneVente> ligneVente = ligneVenteRepository.findAllByArticleId(id);
        if (!ligneVente.isEmpty()) {
        	throw new InvalidOperationException("impossible de supprimer un article déja utilisé dans des ventes", 
        			ErrorCodes.ARTICLE_ALREADY_IN_USE);
        } 
        articleRepository.deleteById(id);
    }
}
