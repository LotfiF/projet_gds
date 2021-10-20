package fr.esic.gestiondestock;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import fr.esic.gestiondestock.model.Adresse;
import fr.esic.gestiondestock.model.Article;
import fr.esic.gestiondestock.model.Category;
import fr.esic.gestiondestock.model.Entreprise;
import fr.esic.gestiondestock.model.Roles;
import fr.esic.gestiondestock.model.Utilisateur;
import fr.esic.gestiondestock.repository.ArticleRepository;
import fr.esic.gestiondestock.repository.CategoryRepository;
import fr.esic.gestiondestock.repository.EntrepriseRepository;
import fr.esic.gestiondestock.repository.RolesRepository;
import fr.esic.gestiondestock.repository.UtilisateurRepository;

@SpringBootApplication
@EnableJpaAuditing
public class GestionDeStockApplication implements CommandLineRunner {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private EntrepriseRepository entrepriseRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Autowired
	private UtilisateurRepository utilisateurRepository;

	public static void main(String[] args) {
		SpringApplication.run(GestionDeStockApplication.class, args);
		
		System.out.println("********************Lancement terminé****************************");
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("********************Lancement du projet**************************");
		
		//Instant inst = Instant.parse("2000-01-01T10:37:30.00Z"); 
		LocalDateTime inst = LocalDateTime.now();  
		Adresse adresse = new Adresse("01, Rue des Halle", "Siege social", "Paris", "75000", "France");
		List<Utilisateur> listUtilisateurs = new ArrayList<Utilisateur>();
		List<Roles> listRoles = new ArrayList<Roles>();

        Entreprise entreprise1 = new Entreprise("mon_entreprise", "description de mon entreprise", adresse, "CodeFiscal", "url_photo_entreprise_cloud", "entreprise@mail.com", "01 23 45 67 89", "www.entreprise.com", listUtilisateurs);
		Utilisateur utilisateur1 = new Utilisateur("nom", "prenom", "utilisateur@mail.com", "motDePasse", inst, adresse, "url_photo_utilisateur_cloud", entreprise1, listRoles);
		Roles role = new Roles("Admin", utilisateur1);
		listRoles.add(role);
		utilisateur1.setRoles(listRoles);
		listUtilisateurs.add(utilisateur1);

		List<Roles> listRoles2 = new ArrayList<Roles>();
		Utilisateur utilisateur2 = new Utilisateur("nom2", "prenom2", "utilisateur2@mail.com", "motDePasse2", inst, adresse, "url_photo_utilisateur2_cloud", entreprise1, listRoles2);
		Roles role2 = new Roles("User", utilisateur2);
		listRoles2.add(role2);
		utilisateur2.setRoles(listRoles2);
		listUtilisateurs.add(utilisateur2);
		
		entreprise1.setUtilisateurs(listUtilisateurs);
		
		entrepriseRepository.save(entreprise1);
		utilisateurRepository.save(utilisateur1);
		utilisateurRepository.save(utilisateur2);
		rolesRepository.save(role);
		rolesRepository.save(role2);
		
		List<Article> listArticles = new ArrayList<Article>();
		Category category1 = new Category("CodeCategory", "DesignationCategory", listArticles, 3);
		Article article1 = new Article("CodeArticle", "DesignationArticle", new BigDecimal("100.0"), new BigDecimal("10.0"), new BigDecimal("110.0"), "url_photo_article_cloud", 3, category1); 
		listArticles.add(article1);
		category1.setArticles(listArticles);
		
		categoryRepository.save(category1);
		articleRepository.save(article1);

		
//		Stream.of(u1, u2, u3, u4).forEach(u-> {
//			userRepo.save(u);	
//		});
	}

}
