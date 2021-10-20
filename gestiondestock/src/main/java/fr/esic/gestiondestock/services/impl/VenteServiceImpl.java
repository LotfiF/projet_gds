package fr.esic.gestiondestock.services.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.esic.gestiondestock.dto.VenteDto;
import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.dto.LigneVenteDto;
import fr.esic.gestiondestock.dto.MvtStkDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Article;
import fr.esic.gestiondestock.model.LigneVente;
import fr.esic.gestiondestock.model.SourceMvtStk;
import fr.esic.gestiondestock.model.TypeMvtStock;
import fr.esic.gestiondestock.model.Vente;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.VenteRepository;
import fr.esic.gestiondestock.repository.LigneVenteRepository;
import fr.esic.gestiondestock.services.MvtStkService;
import fr.esic.gestiondestock.services.VenteService;
import fr.esic.gestiondestock.validator.VenteValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VenteServiceImpl implements VenteService {
	
	private ArticleRepository articleRepository;
	private VenteRepository venteRepository;
	private LigneVenteRepository ligneVenteRepository;
	private MvtStkService mvtStkService; 
	
    @Autowired
    public VenteServiceImpl(
			VenteRepository venteRepository, 
			ArticleRepository articleRepository,
			LigneVenteRepository ligneVenteRepository,
			MvtStkService mvtStkService
		) {
		this.venteRepository = venteRepository;
		this.articleRepository = articleRepository;
		this.ligneVenteRepository = ligneVenteRepository;
		this.mvtStkService = mvtStkService;
    }
    
	
	@Override
	public VenteDto save(VenteDto dto) {
		List<String> errors = VenteValidator.validate(dto);
		if (!errors.isEmpty()) {
			log.error("Vente n'est pas valide");
			throw new InvalidEntityException("l'objet vente n'est pas valide", ErrorCodes.VENTE_NOT_VALID, errors);
		}
		
		List<String> articleErrors = new ArrayList<>();
		
		dto.getLigneVente().forEach(ligneVenteDto -> {
			Optional<Article> article = articleRepository.findById(ligneVenteDto.getArticle().getId());
			if (article.isEmpty()) {
				articleErrors.add("Aucun article avec l'id" + ligneVenteDto.getArticle().getId() + "n'a été trouvé dans la BDD");
			}
		});
		
		if (!articleErrors.isEmpty()) {
			log.error("one or more articles were not found in DB", errors);
			throw new InvalidEntityException("Un ou plusieurs articles n'ont pas été trouvé dans la BDD", 
					ErrorCodes.VENTE_NOT_VALID, errors);
		}
		
		Vente savedVente = venteRepository.save(VenteDto.toEntity(dto));
		
		dto.getLigneVente().forEach(ligneVenteDto -> {
			LigneVente ligneVente = LigneVenteDto.toEntity(ligneVenteDto);
			ligneVente.setVente(savedVente);
			ligneVenteRepository.save(ligneVente);
			updateMvtStk(ligneVente);
		});
		return VenteDto.fromEntity(savedVente);
	}

	@Override
	public VenteDto findById(Integer id) {
        if (id == null){
            log.error("Vente ID is null");
            return null;
        }
        return venteRepository.findById(id)
        		.map(VenteDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune vente avec l'ID = " + id + "n'a été trouvé",
        				ErrorCodes.VENTE_NOT_FOUND)
        		);
    }

	@Override
	public VenteDto findByCode(String code) {
        if (!StringUtils.hasLength(code)){
            log.error("Commande client code is null");
            return null;
        }
        return venteRepository.findVenteByCode(code)
        		.map(VenteDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune vente avec le code = " + code + " n'a été trouvé",
        				ErrorCodes.VENTE_NOT_FOUND)
        		);
	}

	@Override
	public List<VenteDto> findAll() {
        return venteRepository.findAll().stream()
                .map(VenteDto::fromEntity)
                .collect(Collectors.toList());
    }

	@Override
	public void delete(Integer id) {
        if (id == null){
            log.error("Vente Id is null");
            return;
        }
        List<LigneVente> ligneVentes = ligneVenteRepository.findAllByVenteId(id);
        if (!ligneVentes.isEmpty()) {
          throw new InvalidOperationException("Impossible de supprimer une vente déja confirmé",
              ErrorCodes.VENTE_ALREADY_IN_USE);
        }
        venteRepository.deleteById(id);
	}
	
	private void updateMvtStk(LigneVente ligneVente) {
			MvtStkDto mvtStkDto = MvtStkDto.builder()
					.article(ArticleDto.fromEntity(ligneVente.getArticle()))
					.dateMvt(Instant.now())
					.typeMvt(TypeMvtStock.SORTIE)
					.sourceMvt(SourceMvtStk.VENTE)
					.quantite(ligneVente.getQuantite())
					.idEntreprise(ligneVente.getIdEntreprise())
					.build();
			
			mvtStkService.sortieStock(mvtStkDto);
	}

}
