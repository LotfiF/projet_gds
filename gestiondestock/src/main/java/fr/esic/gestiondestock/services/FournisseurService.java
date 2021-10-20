package fr.esic.gestiondestock.services;

import java.util.List;

import fr.esic.gestiondestock.dto.FournisseurDto;

public interface FournisseurService {
	
	FournisseurDto save(FournisseurDto dto);
	
	FournisseurDto findById(Integer id);
	
	List<FournisseurDto> findAll();
	
	void delete(Integer id);

}
