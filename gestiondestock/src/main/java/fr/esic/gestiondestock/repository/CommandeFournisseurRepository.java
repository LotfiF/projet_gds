package fr.esic.gestiondestock.repository;

import fr.esic.gestiondestock.model.CommandeFournisseur;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeFournisseurRepository extends JpaRepository<CommandeFournisseur, Integer> {
	
	Optional<CommandeFournisseur> findCommandeFournisseurByCode(String code);
	
	List<CommandeFournisseur> findAllByFournisseurId(Integer id);
	
}
