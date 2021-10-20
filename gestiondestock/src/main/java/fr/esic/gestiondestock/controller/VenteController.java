package fr.esic.gestiondestock.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import fr.esic.gestiondestock.controller.api.VenteApi;
import fr.esic.gestiondestock.dto.VenteDto;
import fr.esic.gestiondestock.services.VenteService;

@RestController
public class VenteController implements VenteApi {
	
	private VenteService venteService;

	@Autowired
	public VenteController(VenteService utilisateurService) {
		this.venteService = utilisateurService;
	}	
	
	@Override
	public VenteDto save(VenteDto dto) {
		return venteService.save(dto);
	}

	@Override
	public VenteDto findById(Integer id) {
		return venteService.findById(id);
	}

	@Override
	public VenteDto findByCode(String code) {
		 return venteService.findByCode(code);
	}

	@Override
	public List<VenteDto> findAll() {
		 return venteService.findAll();
	}

	@Override
	public void delete(Integer id) {
		venteService.delete(id);
	}

}
