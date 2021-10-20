package fr.esic.gestiondestock.services.impl;

import java.time.Instant;
import java.time.LocalDateTime;  
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esic.gestiondestock.dto.EntrepriseDto;
import fr.esic.gestiondestock.dto.RolesDto;
import fr.esic.gestiondestock.dto.UtilisateurDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.repository.EntrepriseRepository;
import fr.esic.gestiondestock.repository.RolesRepository;
import fr.esic.gestiondestock.services.EntrepriseService;
import fr.esic.gestiondestock.services.UtilisateurService;
import fr.esic.gestiondestock.validator.EntrepriseValidator;
import lombok.extern.slf4j.Slf4j;

@Transactional(rollbackOn = Exception.class)
@Service
@Slf4j
public class EntrepriseServiceImpl implements EntrepriseService{
	

    private EntrepriseRepository entrepriseRepository;
    private UtilisateurService utilisateurService;
    private RolesRepository rolesRepository;

    @Autowired
    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository, UtilisateurService utilisateurService,
    	      RolesRepository rolesRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.utilisateurService = utilisateurService;
        this.rolesRepository = rolesRepository;
    }

    @Override
    public EntrepriseDto save(EntrepriseDto dto) {
        List<String> errors = EntrepriseValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Entreprise is not valid {}", dto);
            throw new InvalidEntityException("l'entreprise n'est pas valide", 
            		ErrorCodes.ENTREPRISE_NOT_VALID, errors);
        }
        EntrepriseDto savedEntreprise = EntrepriseDto.fromEntity(
                entrepriseRepository.save(
                		EntrepriseDto.toEntity(dto)
                ));
        
        UtilisateurDto utilisateur = fromEntreprise(savedEntreprise);
        
        UtilisateurDto savedUser = utilisateurService.save(utilisateur);
        
        RolesDto rolesDto = RolesDto.builder()
        		.roleName("ADMIN")
        		.utilisateur(savedUser)
        		.build();
        
        rolesRepository.save(RolesDto.toEntity(rolesDto));
        		
        return savedEntreprise;
    }
    
    private UtilisateurDto fromEntreprise(EntrepriseDto dto) {
        return UtilisateurDto.builder()
            .adresse(dto.getAdresse())
            .nom(dto.getNom())
            .prenom(dto.getCodeFiscal())
            .mail(dto.getMail())
            .motDePasse(generateRandomPassword())
            .entreprise(dto)
  //        .dateDeNaissance(Instant.now())
            .dateDeNaissance(LocalDateTime.now())
            .photo(dto.getPhoto())
            .build();
      }

      private String generateRandomPassword() {
    	  return "motDePasseAdmin";
//    	  return "$2a$12$vxaiPI.4TnuZcgiq2J55beYTAC/E0f9iWZ5019TMwd9J7MEsado8a";
      }

    @Override
    public EntrepriseDto findById(Integer id) {
        if (id == null){
            log.error("Entreprise ID is null");
            return null;
        }
        return entrepriseRepository.findById(id)
        		.map(EntrepriseDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucune entreprise avec l'ID = " + id + "n'a été trouvé",
        				ErrorCodes.ENTREPRISE_NOT_FOUND)
        		);
    }

    @Override
    public List<EntrepriseDto> findAll() {
        return entrepriseRepository.findAll().stream()
                .map(EntrepriseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Entreprise Id is null");
            return;
        }
        entrepriseRepository.deleteById(id);
    }

}
