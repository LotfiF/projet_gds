package fr.esic.gestiondestock.repository;

import fr.esic.gestiondestock.model.LigneCommandeClient;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeClientRepository extends JpaRepository<LigneCommandeClient, Integer> {
	
	List<LigneCommandeClient> findAllByCommandeClientId(Integer id);
	
	List<LigneCommandeClient> findAllByArticleId(Integer idArticle);
}
