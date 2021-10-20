package fr.esic.gestiondestock.repository;

import fr.esic.gestiondestock.model.Vente;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VenteRepository extends JpaRepository<Vente, Integer> {
	
	Optional<Vente> findVenteByCode(String code);
}
