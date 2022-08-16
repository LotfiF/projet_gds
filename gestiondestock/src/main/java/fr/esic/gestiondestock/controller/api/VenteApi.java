package fr.esic.gestiondestock.controller.api;

import static fr.esic.gestiondestock.utils.Constants.VENTE_ENDPOINT;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.esic.gestiondestock.dto.VenteDto;
import io.swagger.annotations.Api;

@Api("ventes")
public interface VenteApi {
	  	
	  @PostMapping(VENTE_ENDPOINT + "/create")
	  VenteDto save(@RequestBody VenteDto dto);
	  
	  @GetMapping(VENTE_ENDPOINT + "/{idVente}")
	  VenteDto findById(@PathVariable("idVente") Integer id);
	  
	  @GetMapping(VENTE_ENDPOINT + "/filter/{codeVente}")
	  VenteDto findByCode(@PathVariable("codeVente") String code);
	  
	  @GetMapping(VENTE_ENDPOINT + "/all")
	  List<VenteDto> findAll();
	  
	  @DeleteMapping(VENTE_ENDPOINT + "/delete/{idVente}")
	  void delete(@PathVariable("idVente") Integer id);

}
