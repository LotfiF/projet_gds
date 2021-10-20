package fr.esic.gestiondestock.repository;

import fr.esic.gestiondestock.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

//    @Query("select a from article where codearticle = :code and designation = : designation")
//    List<Article> findByCustomQuery(String code, String designation);
//
//    @Query(value = "select a from article where codearticle = :code", nativeQuery = true)
//    List<Article> findByCustomNativeQuery(@Param("code") String c);
//
//    List<Article> findByCodeArticleAndDesignation(String codeArticle, String designation);
//
//    List<Article> findByCodeArticleAndDesignationIgnoreCase(String codeArticle, String designation);

    Optional<Article> findArticleByCodeArticle(String codeArticle);
    
    List<Article> findAllByCategoryId(Integer idCaegory);

}
