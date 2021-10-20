package fr.esic.gestiondestock.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import fr.esic.gestiondestock.dto.ChangerMotDePasseUtilisateurDto;
import fr.esic.gestiondestock.dto.UtilisateurDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.Utilisateur;
import fr.esic.gestiondestock.repository.UtilisateurRepository;
import fr.esic.gestiondestock.services.UtilisateurService;
import fr.esic.gestiondestock.validator.UtilisateurValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UtilisateurServiceImpl implements UtilisateurService {
	
    private UtilisateurRepository utilisateurRepository;
    
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UtilisateurServiceImpl(
    		UtilisateurRepository utilisateurRepository,
    		PasswordEncoder passwordEncoder) {
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UtilisateurDto save(UtilisateurDto dto) {
    	System.out.println(dto);
//    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//    	LocalDateTime newDate = LocalDateTime.parse(dto.getDateDeNaissance(), formatter);
        List<String> errors = UtilisateurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Utilisateur is not valid {}", dto);
            throw new InvalidEntityException("l'utilisateur n'est pas valide", ErrorCodes.UTILISATEUR_NOT_VALID, errors);
        }
        
        if(userAlreadyExists(dto.getMail())) {
            throw new InvalidEntityException(
            		"Un autre utilisateur avec le meme email existe deja", 
            		ErrorCodes.UTILISATEUR_ALREADY_EXISTS,
            		Collections.singletonList("Un autre utilisateur avec le meme email existe deja dans la BDD"));
        }
        
        dto.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));

        return UtilisateurDto.fromEntity(
        		utilisateurRepository.save(
        				UtilisateurDto.toEntity(dto)
                )
        );
    }


	@Override
    public UtilisateurDto findById(Integer id) {
        if (id == null){
            log.error("Utilisateur ID is null");
            return null;
        }
        return utilisateurRepository.findById(id)
        		.map(UtilisateurDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucun utilisateur avec l'ID = " + id + "n'a été trouvé",
        				ErrorCodes.UTILISATEUR_NOT_FOUND)
        		);
    }

    @Override
    public List<UtilisateurDto> findAll() {
        return utilisateurRepository.findAll().stream()
                .map(UtilisateurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Entreprise Id is null");
            return;
        }
        utilisateurRepository.deleteById(id);
    }
    
    @Override
    public UtilisateurDto findByMail(String mail) {
      return utilisateurRepository.findUtilisateurByMail(mail)
          .map(UtilisateurDto::fromEntity)
          .orElseThrow(() -> new EntityNotFoundException(
        		  "Aucun utilisateur avec l'email = " + mail + " n'a été trouvé dans la BDD",
        		  ErrorCodes.UTILISATEUR_NOT_FOUND)
      );
    }

	@Override
	public UtilisateurDto changerMotDePasse(ChangerMotDePasseUtilisateurDto dto) {
		validateUtilisateur(dto);
		Optional<Utilisateur> utilisateurOptional = utilisateurRepository.findById(dto.getId());
		if (utilisateurOptional.isEmpty()) {
			log.warn("Aucun utilisateur n'a été trouvé avec l'ID " + dto.getId());
			throw new EntityNotFoundException("Aucun utilisateur n'a été trouvé avec l'ID " + dto.getId(),
					ErrorCodes.UTILISATEUR_NOT_FOUND);
		}
		Utilisateur utilisateur = utilisateurOptional.get();
		utilisateur.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
		
		return UtilisateurDto.fromEntity(utilisateurRepository.save(utilisateur));
	}
	
	public void validateUtilisateur(ChangerMotDePasseUtilisateurDto dto) {
		if (dto == null) {
			log.warn("Impossible de modifier le mot de Passe avec un objet Null");
			throw new InvalidOperationException("Aucune information n'a été fourni pour pouvoir changer le mot de Passe",
					ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		}
		if (dto.getId() == null) {
			log.warn("Impossible de modifier le mot de Passe avec un ID Null");
			throw new InvalidOperationException("ID Utilisateur Null:: Impossible de mofifier le mot de passe",
					ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		}
		if (!StringUtils.hasLength(dto.getMotDePasse()) || !StringUtils.hasLength(dto.getConfirmMotDePasse())) {
			log.warn("Impossible de modifier le mot de Passe avec un mot de passe Null");
			throw new InvalidOperationException("Mot de passe Utilisateur Null:: Impossible de mofifier le mot de passe",
					ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		}
		
		if (!dto.getMotDePasse().equals(dto.getConfirmMotDePasse())) {
			log.warn("Impossible de modifier le mot de Passe avec deux mots de passe différents");
			throw new InvalidOperationException("Mots de passe Utilisateur Non conforme:: Impossible de mofifier le mot de passe",
					ErrorCodes.UTILISATEUR_CHANGE_PASSWORD_OBJECT_NOT_VALID);
		}
	}
	
	private boolean userAlreadyExists(String email) {
	    Optional<Utilisateur> user = utilisateurRepository.findUtilisateurByMail(email);
	    return user.isPresent();
	}

}
