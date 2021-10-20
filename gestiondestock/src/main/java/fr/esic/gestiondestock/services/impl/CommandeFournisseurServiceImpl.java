package fr.esic.gestiondestock.services.impl;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.dto.CommandeFournisseurDto;
import fr.esic.gestiondestock.dto.FournisseurDto;
import fr.esic.gestiondestock.dto.LigneCommandeFournisseurDto;
import fr.esic.gestiondestock.dto.MvtStkDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Article;
import fr.esic.gestiondestock.model.CommandeFournisseur;
import fr.esic.gestiondestock.model.EtatCommande;
import fr.esic.gestiondestock.model.Fournisseur;
import fr.esic.gestiondestock.model.LigneCommandeFournisseur;
import fr.esic.gestiondestock.model.SourceMvtStk;
import fr.esic.gestiondestock.model.TypeMvtStock;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.CommandeFournisseurRepository;
import fr.esic.gestiondestock.repository.FournisseurRepository;
import fr.esic.gestiondestock.repository.LigneCommandeFournisseurRepository;
import fr.esic.gestiondestock.services.CommandeFournisseurService;
import fr.esic.gestiondestock.services.MvtStkService;
import fr.esic.gestiondestock.validator.ArticleValidator;
import fr.esic.gestiondestock.validator.CommandeFournisseurValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommandeFournisseurServiceImpl implements CommandeFournisseurService {
	
	private CommandeFournisseurRepository commandeFournisseurRepository;
	private LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository;
	private FournisseurRepository fournisseurRepository;
	private ArticleRepository articleRepository;
	private MvtStkService mvtStkService; 
	
	@Autowired
	public CommandeFournisseurServiceImpl(CommandeFournisseurRepository commandeFournisseurRepository,
			LigneCommandeFournisseurRepository ligneCommandeFournisseurRepository, FournisseurRepository fournisseurRepository, 
			ArticleRepository articleRepository, MvtStkService mvtStkService) {
		this.commandeFournisseurRepository = commandeFournisseurRepository;
		this.ligneCommandeFournisseurRepository = ligneCommandeFournisseurRepository;
		this.fournisseurRepository = fournisseurRepository;
		this.articleRepository = articleRepository;
		this.mvtStkService = mvtStkService;
	}

	@Override
	public CommandeFournisseurDto save(CommandeFournisseurDto dto) {
		List<String> errors = CommandeFournisseurValidator.validate(dto);

	    if (!errors.isEmpty()) {
	      log.error("Commande fournisseur n'est pas valide");
	      throw new InvalidEntityException(
	    		  "La commande fournisseur n'est pas valide", ErrorCodes.COMMANDE_FOURNISSEUR_NOT_VALID, errors);
	    }
	    
		if (dto.getId() != null && dto.isCommandeLivree()) {
			throw new InvalidOperationException(
					"Impossible de modifier la commande lorsqu'elle est livree", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}

	    Optional<Fournisseur> fournisseur = fournisseurRepository.findById(dto.getFournisseur().getId());
	    
	    if (fournisseur.isEmpty()) {
	      log.warn("Fournisseur with ID {} was not found in the DB", dto.getFournisseur().getId());
	      throw new EntityNotFoundException("Aucun fournisseur avec l'ID" + dto.getFournisseur().getId() + " n'a ete trouve dans la BDD",
	          ErrorCodes.FOURNISSEUR_NOT_FOUND);
	    }

	    List<String> articleErrors = new ArrayList<>();

	    if (dto.getLigneCommandeFournisseurs() != null) {
	      dto.getLigneCommandeFournisseurs().forEach(ligneCommandeFournisseur -> {
	        if (ligneCommandeFournisseur.getArticle() != null) {
	          Optional<Article> article = articleRepository.findById(ligneCommandeFournisseur.getArticle().getId());
	          if (article.isEmpty()) {
	            articleErrors.add("L'article avec l'ID " + ligneCommandeFournisseur.getArticle().getId() + " n'existe pas");
	          }
	        } else {
	          articleErrors.add("Impossible d'enregister une commande avec un aticle NULL");
	        }
	      });
	    }

	    if (!articleErrors.isEmpty()) {
	      log.warn("");
	      throw new InvalidEntityException("Article n'existe pas dans la BDD", 
	    		  ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
	    }
	    
	    dto.setDateCommande(Instant.now());
	    CommandeFournisseur savedCommandeFournisseur = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(dto));
	    
	    if (dto.getLigneCommandeFournisseurs() != null) {
	      dto.getLigneCommandeFournisseurs().forEach(ligCmdFrs -> {
	        LigneCommandeFournisseur ligneCommandeFournisseur = LigneCommandeFournisseurDto.toEntity(ligCmdFrs);
	        ligneCommandeFournisseur.setCommandeFournisseur(savedCommandeFournisseur);
	        ligneCommandeFournisseur.setIdEntreprise(dto.getIdEntreprise());
	        LigneCommandeFournisseur savedLigneCommande = ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
	        System.out.println(savedLigneCommande); 
	        effectuerEntree(savedLigneCommande);
	      });
	    }

	    return CommandeFournisseurDto.fromEntity(savedCommandeFournisseur);

	}

	@Override
	public CommandeFournisseurDto findById(Integer id) {
		if (id == null) {
			log.error("Commande fournisseur ID is NULL");
			return null;
		}
		return commandeFournisseurRepository.findById(id)
				.map(CommandeFournisseurDto::fromEntity)
				.orElseThrow(() -> new EntityNotFoundException
						("Aucune commande fournisseur n'a ete trouve avec l'ID " + id,
						ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND));
	}

	@Override
	public CommandeFournisseurDto findByCode(String code) {
		if (!StringUtils.hasLength(code)) {
		      log.error("Commande fournisseur CODE is NULL");
		      return null;
		    }
		    return commandeFournisseurRepository.findCommandeFournisseurByCode(code)
		        .map(CommandeFournisseurDto::fromEntity)
		        .orElseThrow(() -> new EntityNotFoundException(
		            "Aucune commande fournisseur n'a ete trouve avec le CODE " + code, 
		            ErrorCodes.COMMANDE_FOURNISSEUR_NOT_FOUND)
		        );
	}

	@Override
	public List<CommandeFournisseurDto> findAll() {
	    return commandeFournisseurRepository.findAll().stream()
	            .map(CommandeFournisseurDto::fromEntity)
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<LigneCommandeFournisseurDto> findAllLignesCommandesFournisseurByCommandeFournisseurId(Integer idCommande) {
		System.out.println("je suis service find");
		return ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande).stream()
				.map(LigneCommandeFournisseurDto::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
        if (id == null){
            log.error("Commande Fournisseur Id is null");
            return;
        }
        List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByArticleId(id);
        if (!ligneCommandeFournisseurs.isEmpty()) {
        	throw new InvalidOperationException("Impossible de supprimer une commande fournisseur déja confirmé", 
        			ErrorCodes.COMMANDE_FOURNISSEUR_ALREADY_IN_USE);
        }
        commandeFournisseurRepository.deleteById(id);
		
	}
	
	@Override
	public CommandeFournisseurDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
		
		checkIdCommande(idCommande);
		
		if (StringUtils.hasLength(String.valueOf(etatCommande))){
			log.error("L'état de la Commande is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un etat NULL", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
		
		CommandeFournisseurDto commandeFournisseur = checkEtatCommande(idCommande);
		
		commandeFournisseur.setEtatCommande(etatCommande);
		
		CommandeFournisseur savedCommandeFournisseur = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseur));
		if (commandeFournisseur.isCommandeLivree()) {
			updateMvtStk(idCommande);
		}
		return CommandeFournisseurDto.fromEntity(savedCommandeFournisseur);
	}
	
	@Override
	public CommandeFournisseurDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
		
		checkIdCommande(idCommande);
		
		checkIdLigneCommande(idLigneCommande);
		
		if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
			log.error("L' ID de la Commande fournisseur is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec une quantité NULL ou ZERO", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
		
		CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
		
		LigneCommandeFournisseur ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);
		
		ligneCommandeFournisseur.setQuantite(quantite);
		
		ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
		
		return commandeFournisseurDto;
	}
	
	@Override
	public CommandeFournisseurDto updateFournisseur(Integer idCommande, Integer idFournisseur) {
		
		checkIdCommande(idCommande);
		
		checkIdFournisseur(idFournisseur);
		
		CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
		
		Fournisseur fournisseur = findFournisseur(idFournisseur);
		
		commandeFournisseurDto.setFournisseur(FournisseurDto.fromEntity(fournisseur));
		
		CommandeFournisseur commandeFournisseur = commandeFournisseurRepository.save(CommandeFournisseurDto.toEntity(commandeFournisseurDto));
		
		return CommandeFournisseurDto.fromEntity(commandeFournisseur);
		
	}

	@Override
	public CommandeFournisseurDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
		checkIdCommande(idCommande);
		checkIdLigneCommande(idLigneCommande);
		checkIdArticle(idArticle);
		
		CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
		
		LigneCommandeFournisseur ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);
		Article article = findArticle(idArticle);
		
		List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(article));
		if (errors.isEmpty()) {
			throw new InvalidEntityException("", ErrorCodes.ARTICLE_NOT_VALID, errors);
		}
		
		ligneCommandeFournisseur.setArticle(article);
		
		ligneCommandeFournisseurRepository.save(ligneCommandeFournisseur);
		
		return commandeFournisseurDto;
	}
	
	@Override
	public CommandeFournisseurDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
		checkIdCommande(idCommande);
		checkIdLigneCommande(idLigneCommande);
		
		CommandeFournisseurDto commandeFournisseurDto = checkEtatCommande(idCommande);
		// Just to check the LigneCommandeFournisseur and inform the user in case it is absent
		LigneCommandeFournisseur ligneCommandeFournisseur = findLigneCommandeFournisseur(idLigneCommande);
		ligneCommandeFournisseurRepository.deleteById(idLigneCommande);
		
		return commandeFournisseurDto;
	}
	
	private void checkIdCommande(Integer idCommande) {
		if (idCommande == null) {
			log.error("Commande Fournisseur ID is null");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un ID COMMANDE NULL", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
	}
	
	private void checkIdLigneCommande(Integer idLigneCommande) {
		if (idLigneCommande == null){
			log.error("L'ID de la ligne commande fournisseur is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec une ligne de commande NULL", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
	}
	
	private void checkIdFournisseur(Integer idFournisseur) {
		if (idFournisseur == null){
			log.error("L'ID du fournisseur is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un ID fournisseur NULL", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
	}
	
	private void checkIdArticle(Integer idArticle) {
		if (idArticle == null){
			log.error("L'ID de l'article is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un ID article NULL", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
	}
	
	private CommandeFournisseurDto checkEtatCommande(Integer idCommande){
		CommandeFournisseurDto commandeFournisseur = findById(idCommande);
		if (commandeFournisseur.isCommandeLivree()) {
			throw new InvalidOperationException(
					"Impossible de modifier la commande lorsqu'elle est livree", 
					ErrorCodes.COMMANDE_FOURNISSEUR_NOT_MODIFIABLE
					);	
		}
		return commandeFournisseur;
	}
	
	private LigneCommandeFournisseur findLigneCommandeFournisseur(Integer idLigneCommande) {
		
		Optional<LigneCommandeFournisseur> ligneCommandeFournisseurOptional = ligneCommandeFournisseurRepository.findById(idLigneCommande);
		
		if (ligneCommandeFournisseurOptional.isEmpty()) {
			throw new EntityNotFoundException(
					"Aucune ligne Commande Fournisseur n'a été trouvé avec l'ID " + idLigneCommande, 
					ErrorCodes.LIGNE_COMMANDE_FOURNISSEUR_NOT_FOUND
					);
		}
		
		return ligneCommandeFournisseurOptional.get();
	}
	
	private Article findArticle(Integer idArticle) {
		
		Optional<Article> articleOptional = articleRepository.findById(idArticle);
		
		if (articleOptional.isEmpty()) {
			throw new EntityNotFoundException(
					"Aucune article n'a été trouvé avec l'ID " + idArticle, 
					ErrorCodes.ARTICLE_NOT_FOUND
					);
		}
		
		return articleOptional.get();
	}
	
	private Fournisseur findFournisseur(Integer idFournisseur) {
		Optional<Fournisseur> fournisseurOptional = fournisseurRepository.findById(idFournisseur);
		
		if (fournisseurOptional.isEmpty()) {
			throw new EntityNotFoundException(
					"Aucune fournisseur n'a été trouvé avec l'ID " + idFournisseur, 
					ErrorCodes.FOURNISSEUR_NOT_FOUND);
		}
		return fournisseurOptional.get();		
	}
	
	private void updateMvtStk(Integer idCommande) {
		List<LigneCommandeFournisseur> ligneCommandeFournisseurs = ligneCommandeFournisseurRepository.findAllByCommandeFournisseurId(idCommande);
		ligneCommandeFournisseurs.forEach(lig -> {
			 effectuerEntree(lig);
		});
	}
	
	private void effectuerEntree(LigneCommandeFournisseur lig) {
	    MvtStkDto mvtStkDto = MvtStkDto.builder()
	        .article(ArticleDto.fromEntity(lig.getArticle()))
	        .dateMvt(Instant.now())
	        .typeMvt(TypeMvtStock.ENTREE)
	        .sourceMvt(SourceMvtStk.COMMANDE_FOURNISSEUR)
	        .quantite(lig.getQuantite())
	        .idEntreprise(lig.getIdEntreprise())
	        .build();
	    mvtStkService.entreeStock(mvtStkDto);
	  }

}
