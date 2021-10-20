package fr.esic.gestiondestock.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import fr.esic.gestiondestock.controller.api.MvtStkApi;
import fr.esic.gestiondestock.dto.MvtStkDto;
import fr.esic.gestiondestock.services.MvtStkService;

@RestController
public class MvtStkController implements MvtStkApi {
	
	private MvtStkService mvtStkService;
	
	public MvtStkController(MvtStkService mvtStkService) {
		this.mvtStkService = mvtStkService;
	}

	@Override
	public BigDecimal stockReelArticle(Integer idArticle) {
		return mvtStkService.stockReelArticle(idArticle);
	}

	@Override
	public List<MvtStkDto> mvtStkArticle(Integer idArticle) {
		return mvtStkService.mvtStkArticle(idArticle);
	}

	@Override
	public MvtStkDto entreeStock(MvtStkDto mvtStkDto) {
		return mvtStkService.entreeStock(mvtStkDto);
	}

	@Override
	public MvtStkDto sortieStock(MvtStkDto mvtStkDto) {
		return mvtStkService.sortieStock(mvtStkDto);
	}

	@Override
	public MvtStkDto correctionStockPos(MvtStkDto mvtStkDto) {
		return mvtStkService.correctionStockPos(mvtStkDto);
	}

	@Override
	public MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto) {
		return mvtStkService.correctionStockNeg(mvtStkDto);
	}

}
