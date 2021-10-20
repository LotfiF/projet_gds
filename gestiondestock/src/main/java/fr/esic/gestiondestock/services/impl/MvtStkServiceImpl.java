package fr.esic.gestiondestock.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esic.gestiondestock.dto.MvtStkDto;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.model.TypeMvtStock;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.MvtStkRepository;
import fr.esic.gestiondestock.services.MvtStkService;
import fr.esic.gestiondestock.validator.MvtStkValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MvtStkServiceImpl implements MvtStkService {
	
	private MvtStkRepository mvtStkRepository;
	private ArticleRepository articleRepository;
	
	@Autowired
	public MvtStkServiceImpl(MvtStkRepository mvtStkRepository, ArticleRepository articleRepository) {
		this.mvtStkRepository = mvtStkRepository;
		this.articleRepository = articleRepository;
	}

	@Override
	public BigDecimal stockReelArticle(Integer idArticle) {
		if (idArticle == null) {
			log.warn("ID article is Null");
			return BigDecimal.valueOf(-1); 
		}
		articleRepository.findById(idArticle);
		return mvtStkRepository.stockReelArticle(idArticle);
	}

	@Override
	public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
		// TODO Auto-generated method stub
		return mvtStkRepository.findAllByArticleId(idArticle).stream()
				.map(MvtStkDto::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public MvtStkDto entreeStock(MvtStkDto mvtStkDto) {
		return mouvement(mvtStkDto, TypeMvtStock.ENTREE, 1);
	}

	@Override
	public MvtStkDto sortieStock(MvtStkDto mvtStkDto) {
		return mouvement(mvtStkDto, TypeMvtStock.SORTIE, -1);
	}

	@Override
	public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) {
		return mouvement(mvtStkDto, TypeMvtStock.CORRECTION_POS, 1);
	}

	@Override
	public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) {
		return mouvement(mvtStkDto, TypeMvtStock.CORRECTION_NEG, -1);
	}
	
	private MvtStkDto mouvement(MvtStkDto mvtStkDto, TypeMvtStock typeMvtStk, Integer coef) {
		List<String> errors = MvtStkValidator.validate(mvtStkDto); 
		if (!errors.isEmpty()) {
		      log.error("MvtStk is not valid {}", mvtStkDto);
		      throw new InvalidEntityException("Le mouvement du stock n'est pas valide", ErrorCodes.MVT_STK_NOT_VALID, errors);
		}
		mvtStkDto.setQuantite(
				BigDecimal.valueOf(
						Math.abs(mvtStkDto.getQuantite().doubleValue()) * coef
						)
				);
		mvtStkDto.setTypeMvt(typeMvtStk);
		return MvtStkDto.fromEntity(mvtStkRepository.save(MvtStkDto.toEntity(mvtStkDto)));
		
	}

}
