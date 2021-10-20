package fr.esic.gestiondestock.repository;

import fr.esic.gestiondestock.model.LigneCommandeFournisseur;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LigneCommandeFournisseurRepository extends JpaRepository<LigneCommandeFournisseur, Integer> {
	
	List<LigneCommandeFournisseur> findAllByCommandeFournisseurId(Integer id);
	
	List<LigneCommandeFournisseur> findAllByArticleId(Integer idArticle);
}
