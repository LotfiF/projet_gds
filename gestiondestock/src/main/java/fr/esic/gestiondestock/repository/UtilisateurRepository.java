package fr.esic.gestiondestock.repository;

import fr.esic.gestiondestock.model.Utilisateur;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
	
	  // JPQL query
	   @Query(value = "select u from Utilisateur u where u.mail = :mail")
	   Optional<Utilisateur> findUtilisateurByMail(@Param("mail") String mail);
	  
//	  Optional<Utilisateur> findUtilisateurByMail(String mail);
}
