package fr.esic.gestiondestock.services;

import java.util.List;

import fr.esic.gestiondestock.dto.VenteDto;

public interface VenteService {
	
    VenteDto save(VenteDto dto);

    VenteDto findById(Integer id);

    VenteDto findByCode(String code);

    List<VenteDto> findAll();

    void delete(Integer id);

}
