package fr.esic.gestiondestock.services;

import java.math.BigDecimal;
import java.util.List;

import fr.esic.gestiondestock.dto.MvtStkDto;

public interface MvtStkService {
	
	BigDecimal stockReelArticle(Integer idArticle);
	
	List<MvtStkDto> mvtStkArticle(Integer idArticle);
	
	MvtStkDto entreeStock(MvtStkDto mvtStkDto);
	
	MvtStkDto sortieStock(MvtStkDto mvtStkDto);
	
	MvtStkDto correctionStockPos(MvtStkDto mvtStkDto);

	MvtStkDto correctionStockNeg(MvtStkDto mvtStkDto);

}
