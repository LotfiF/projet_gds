package fr.esic.gestiondestock.controller.api;

import static fr.esic.gestiondestock.utils.Constants.ENTREPRISE_ENDPOINT;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import fr.esic.gestiondestock.dto.EntrepriseDto;
import io.swagger.annotations.Api;

@Api("entreprises")
public interface EntrepriseApi {
	  
	  @PostMapping(ENTREPRISE_ENDPOINT + "/create")
	  EntrepriseDto save(@RequestBody EntrepriseDto dto);

	  @GetMapping(ENTREPRISE_ENDPOINT + "/{idEntreprise}")
	  EntrepriseDto findById(@PathVariable("idEntreprise") Integer id);

	  @GetMapping(ENTREPRISE_ENDPOINT + "/all")
	  List<EntrepriseDto> findAll();

	  @DeleteMapping(ENTREPRISE_ENDPOINT + "/delete/{idEntreprise}")
	  void delete(@PathVariable("idEntreprise") Integer id);

}
