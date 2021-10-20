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
import fr.esic.gestiondestock.dto.ClientDto;
import fr.esic.gestiondestock.dto.CommandeClientDto;
import fr.esic.gestiondestock.dto.LigneCommandeClientDto;
import fr.esic.gestiondestock.dto.MvtStkDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Article;
import fr.esic.gestiondestock.model.Client;
import fr.esic.gestiondestock.model.CommandeClient;
import fr.esic.gestiondestock.model.EtatCommande;
import fr.esic.gestiondestock.model.LigneCommandeClient;
import fr.esic.gestiondestock.model.SourceMvtStk;
import fr.esic.gestiondestock.model.TypeMvtStock;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.ClientRepository;
import fr.esic.gestiondestock.repository.CommandeClientRepository;
import fr.esic.gestiondestock.repository.LigneCommandeClientRepository;
import fr.esic.gestiondestock.services.CommandeClientService;
import fr.esic.gestiondestock.services.MvtStkService;
import fr.esic.gestiondestock.validator.ArticleValidator;
import fr.esic.gestiondestock.validator.CommandeClientValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommandeClientServiceImpl implements CommandeClientService {
	
	private CommandeClientRepository commandeClientRepository;
	private LigneCommandeClientRepository ligneCommandeClientRepository;
	private ClientRepository clientRepository;
	private ArticleRepository articleRepository;
	private MvtStkService mvtStkService;
	
	@Autowired	
	public CommandeClientServiceImpl(CommandeClientRepository commandeClientRepository,
			LigneCommandeClientRepository ligneCommandeClientRepository, ClientRepository clientRepository,
			ArticleRepository articleRepository, MvtStkService mvtStkService) {
		this.commandeClientRepository = commandeClientRepository;
		this.ligneCommandeClientRepository = ligneCommandeClientRepository;
		this.clientRepository = clientRepository;
		this.articleRepository = articleRepository;
		this.mvtStkService = mvtStkService;
	}

	@Override
	public CommandeClientDto save(CommandeClientDto dto) {
		List<String> errors = CommandeClientValidator.validate(dto);
		
		if (!errors.isEmpty()) {
			log.error("Commande client n'est pas valide");
			 throw new InvalidEntityException("la commande client n'est pas valide", 
					 ErrorCodes.COMMANDE_CLIENT_NOT_VALID, errors);
        }
		
		if (dto.getId() != null && dto.isCommandeLivree()) {
			throw new InvalidOperationException(
					"Impossible de modifier la commande lorsqu'elle est livree", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
		
		Optional<Client> client = clientRepository.findById(dto.getClient().getId());
		
		if (client.isEmpty()) {
			log.warn("Client with ID {} was not found in the DB", dto.getClient().getId());
			throw new EntityNotFoundException("Aucun client avec l'ID" + dto.getClient().getId() + "n'a été trouvé dans la BD", 
					ErrorCodes.CLIENT_NOT_FOUND);
		}
		
		List<String> articleErrors = new ArrayList<>();
		
		if (dto.getLigneCommandeClients() != null) {
			dto.getLigneCommandeClients().forEach(ligneCommandeClient -> {
				if (ligneCommandeClient.getArticle() != null) {
					Optional<Article> article = articleRepository.findById(ligneCommandeClient.getArticle().getId());
					if (article.isEmpty()) {
						articleErrors.add("l'article avec le id " + ligneCommandeClient.getArticle().getId() + "n'existe pas");
					}
				} else {
					articleErrors.add("impossible d'enregistrer une commande avec un article NULL");
				}
			});
		}
		
		if (!articleErrors.isEmpty()) {
			log.warn("");
			throw new InvalidEntityException("Article n'existe pas dans la BDD", 
					ErrorCodes.ARTICLE_NOT_FOUND, articleErrors);
		}
		
		dto.setDateCommande(Instant.now());
		CommandeClient savedCommandeClient = commandeClientRepository.save(CommandeClientDto.toEntity(dto));
		
		if (dto.getLigneCommandeClients() != null) {
			System.out.println("I am Inside IF Client");
			dto.getLigneCommandeClients().forEach(ligCmdClt -> {
				LigneCommandeClient ligneCommandeClient = LigneCommandeClientDto.toEntity(ligCmdClt);
				ligneCommandeClient.setCommandeClient(savedCommandeClient);
				ligneCommandeClient.setIdEntreprise(dto.getIdEntreprise());
				LigneCommandeClient savedLigneCommande = ligneCommandeClientRepository.save(ligneCommandeClient);
				effectuerSortie(savedLigneCommande);
			});
		}
		
		return CommandeClientDto.fromEntity(savedCommandeClient);
	}

	@Override
	public CommandeClientDto findById(Integer id) {
        if (id == null){
            log.error("Commande Client ID is null");
            return null;
        }
        return commandeClientRepository.findById(id)
        		.map(CommandeClientDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune commande client avec l'ID = " + id + "n'a été trouvé",
        				ErrorCodes.COMMANDE_CLIENT_NOT_FOUND)
        		);
    }

	@Override
	public CommandeClientDto findByCode(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Commande client code is null");
            return null;
        }
        return commandeClientRepository.findCommandeClientByCode(code)
        		.map(CommandeClientDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune commande client avec le CODE = " + code + " n'a été trouvé",
        				ErrorCodes.COMMANDE_CLIENT_NOT_FOUND)
        		);
	}

	@Override
	public List<CommandeClientDto> findAll() {
        return commandeClientRepository.findAll().stream()
                .map(CommandeClientDto::fromEntity)
                .collect(Collectors.toList());
    }
	
	
	@Override
	public List<LigneCommandeClientDto> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
		return ligneCommandeClientRepository.findAllByCommandeClientId(idCommande).stream()
				.map(LigneCommandeClientDto::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public void delete(Integer id) {
        if (id == null){
            log.error("Commande Client Id is null");
            return;
        }
        List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByArticleId(id);
        if (!ligneCommandeClients.isEmpty()) {
        	throw new InvalidOperationException("Impossible de supprimer une commande client déja confirmé", 
        			ErrorCodes.COMMANDE_CLIENT_ALREADY_IN_USE);
        }
        commandeClientRepository.deleteById(id);
	}

	@Override
	public CommandeClientDto updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
		
		checkIdCommande(idCommande);
		
		if (StringUtils.hasLength(String.valueOf(etatCommande))){
			log.error("L'état de la Commande is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un etat NULL", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
		
		CommandeClientDto commandeClient = checkEtatCommande(idCommande);
		
		commandeClient.setEtatCommande(etatCommande);
		
		CommandeClient savedCommandeClient = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClient));
		if (commandeClient.isCommandeLivree()) {
			updateMvtStk(idCommande);
		}
		return CommandeClientDto.fromEntity(savedCommandeClient);
	}

	@Override
	public CommandeClientDto updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
		
		checkIdCommande(idCommande);
		
		checkIdLigneCommande(idLigneCommande);
		
		if (quantite == null || quantite.compareTo(BigDecimal.ZERO) == 0) {
			log.error("L' ID de la Commande client is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec une quantité NULL ou ZERO", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
		
		CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);
		
		LigneCommandeClient ligneCommandeClient = findLigneCommandeClient(idLigneCommande);
		
		ligneCommandeClient.setQuantite(quantite);
		
		ligneCommandeClientRepository.save(ligneCommandeClient);
		
		return commandeClientDto;
	}

	@Override
	public CommandeClientDto updateClient(Integer idCommande, Integer idClient) {
		
		checkIdCommande(idCommande);
		
		checkIdClient(idClient);
		
		CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);
		
		Client client = findClient(idClient);
		
		commandeClientDto.setClient(ClientDto.fromEntity(client));
		
		CommandeClient commandeClient = commandeClientRepository.save(CommandeClientDto.toEntity(commandeClientDto));
		
		return CommandeClientDto.fromEntity(commandeClient);
		
	}

	@Override
	public CommandeClientDto updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
		checkIdCommande(idCommande);
		checkIdLigneCommande(idLigneCommande);
		checkIdArticle(idArticle);
		
		CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);
		
		LigneCommandeClient ligneCommandeClient = findLigneCommandeClient(idLigneCommande);
		Article article = findArticle(idArticle);
		
		List<String> errors = ArticleValidator.validate(ArticleDto.fromEntity(article));
		if (errors.isEmpty()) {
			throw new InvalidEntityException("", ErrorCodes.ARTICLE_NOT_VALID, errors);
		}
		
		ligneCommandeClient.setArticle(article);
		
		ligneCommandeClientRepository.save(ligneCommandeClient);
		
		return commandeClientDto;
	}
	
	@Override
	public CommandeClientDto deleteArticle(Integer idCommande, Integer idLigneCommande) {
		checkIdCommande(idCommande);
		checkIdLigneCommande(idLigneCommande);
		
		CommandeClientDto commandeClientDto = checkEtatCommande(idCommande);
		// Just to check the LigneCommandeClient and inform the client in case it is absent
		LigneCommandeClient ligneCommandeClient = findLigneCommandeClient(idLigneCommande);
		ligneCommandeClientRepository.deleteById(idLigneCommande);
		
		return commandeClientDto;
	}
	
	private void checkIdCommande(Integer idCommande) {
		if (idCommande == null) {
			log.error("Commande Client ID is null");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un ID COMMANDE NULL", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
	}
	
	private void checkIdLigneCommande(Integer idLigneCommande) {
		if (idLigneCommande == null){
			log.error("L'ID de la ligne commande client is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec une ligne de commande NULL", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
	}
	
	private void checkIdClient(Integer idClient) {
		if (idClient == null){
			log.error("L'ID du client is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un ID client NULL", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
	}
	
	private void checkIdArticle(Integer idArticle) {
		if (idArticle == null){
			log.error("L'ID de l'article is NULL");
			throw new InvalidOperationException(
					"Impossible de modifier l'état de la commande avec un ID article NULL", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
	}
	
	private CommandeClientDto checkEtatCommande(Integer idCommande){
		CommandeClientDto commandeClient = findById(idCommande);
		if (commandeClient.isCommandeLivree()) {
			throw new InvalidOperationException(
					"Impossible de modifier la commande lorsqu'elle est livree", 
					ErrorCodes.COMMANDE_CLIENT_NOT_MODIFIABLE
					);	
		}
		return commandeClient;
	}
	
	private LigneCommandeClient findLigneCommandeClient(Integer idLigneCommande) {
		
		Optional<LigneCommandeClient> ligneCommandeClientOptional = ligneCommandeClientRepository.findById(idLigneCommande);
		
		if (ligneCommandeClientOptional.isEmpty()) {
			throw new EntityNotFoundException(
					"Aucune ligne Commande Client n'a été trouvé avec l'ID " + idLigneCommande, 
					ErrorCodes.LIGNE_COMMANDE_CLIENT_NOT_FOUND
					);
		}
		
		return ligneCommandeClientOptional.get();
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
	
	private Client findClient(Integer idClient) {
		Optional<Client> clientOptional = clientRepository.findById(idClient);
		
		if (clientOptional.isEmpty()) {
			throw new EntityNotFoundException(
					"Aucune client n'a été trouvé avec l'ID " + idClient, 
					ErrorCodes.CLIENT_NOT_FOUND);
		}
		return clientOptional.get();		
	}
	
	private void updateMvtStk(Integer idCommande) {
		List<LigneCommandeClient> ligneCommandeClients = ligneCommandeClientRepository.findAllByCommandeClientId(idCommande);
		ligneCommandeClients.forEach(lig -> {
			MvtStkDto mvtStkDto = MvtStkDto.builder()
					.article(ArticleDto.fromEntity(lig.getArticle()))
					.dateMvt(Instant.now())
					.typeMvt(TypeMvtStock.SORTIE)
					.sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
					.quantite(lig.getQuantite())
					.idEntreprise(lig.getIdEntreprise())
					.build();
			
			mvtStkService.sortieStock(mvtStkDto);
		});
	}
	
	private void effectuerSortie(LigneCommandeClient lig) {
	    MvtStkDto mvtStkDto = MvtStkDto.builder()
	        .article(ArticleDto.fromEntity(lig.getArticle()))
	        .dateMvt(Instant.now())
	        .typeMvt(TypeMvtStock.SORTIE)
	        .sourceMvt(SourceMvtStk.COMMANDE_CLIENT)
	        .quantite(lig.getQuantite())
	        .idEntreprise(lig.getIdEntreprise())
	        .build();
	    mvtStkService.sortieStock(mvtStkDto);
	  }

}
