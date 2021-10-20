package fr.esic.gestiondestock.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esic.gestiondestock.dto.FournisseurDto;
import fr.esic.gestiondestock.exception.EntityNotFoundException;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidEntityException;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.model.CommandeFournisseur;
import fr.esic.gestiondestock.repository.CommandeFournisseurRepository;
import fr.esic.gestiondestock.repository.FournisseurRepository;
import fr.esic.gestiondestock.services.FournisseurService;
import fr.esic.gestiondestock.validator.FournisseurValidator;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FournisseurServiceImpl implements FournisseurService {
	
    private FournisseurRepository fournisseurRepository;
    private CommandeFournisseurRepository commandeFournisseurRepository;

    @Autowired
    public FournisseurServiceImpl(FournisseurRepository fournisseurRepository,
			CommandeFournisseurRepository commandeFournisseurRepository) {
		this.fournisseurRepository = fournisseurRepository;
		this.commandeFournisseurRepository = commandeFournisseurRepository;
	}

	@Override
    public FournisseurDto save(FournisseurDto dto) {
        List<String> errors = FournisseurValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Fournisseur is not valid {}", dto);
            throw new InvalidEntityException("le fournisseur n'est pas valide", ErrorCodes.FOURNISSEUR_NOT_VALID, errors);
        }
        return FournisseurDto.fromEntity(
        		fournisseurRepository.save(
                		FournisseurDto.toEntity(dto)
                )
        );
    }

    @Override
    public FournisseurDto findById(Integer id) {
        if (id == null){
            log.error("Fournisseur ID is null");
            return null;
        }
        return fournisseurRepository.findById(id)
        		.map(FournisseurDto::fromEntity)
        		.orElseThrow(() -> new EntityNotFoundException(
        				"Aucun fournisseur avec l'ID = " + id + "n'a été trouvé",
        				ErrorCodes.FOURNISSEUR_NOT_FOUND)
        		);
    }

    @Override
    public List<FournisseurDto> findAll() {
        return fournisseurRepository.findAll().stream()
                .map(FournisseurDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null){
            log.error("Fournisseur Id is null");
            return;
        }
        List<CommandeFournisseur> commandesFournisseurs = commandeFournisseurRepository.findAllByFournisseurId(id);
                                                                                        
        if (!commandesFournisseurs.isEmpty()) {
        	throw new InvalidOperationException("impossible de supprimer un founrisseur utilisé dans des commandes fournisseurs", 
        			ErrorCodes.FOURNISSEUR_ALREADY_IN_USE);
        }
        fournisseurRepository.deleteById(id);
    }
	


}
