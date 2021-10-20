package fr.esic.gestiondestock.services;

import java.util.List;

import fr.esic.gestiondestock.dto.EntrepriseDto;

public interface EntrepriseService {
	
	EntrepriseDto save(EntrepriseDto dto);
	
	EntrepriseDto findById(Integer id);
	
	List<EntrepriseDto> findAll();
	
	void delete(Integer id);

}
