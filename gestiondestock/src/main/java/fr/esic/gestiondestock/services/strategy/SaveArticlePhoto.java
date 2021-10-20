package fr.esic.gestiondestock.services.strategy;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.flickr4java.flickr.FlickrException;

import fr.esic.gestiondestock.dto.ArticleDto;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.services.ArticleService;
import fr.esic.gestiondestock.services.FlickrService;
import lombok.extern.slf4j.Slf4j;

@Service("articleStrategy")
@Slf4j
public class SaveArticlePhoto implements Strategy<ArticleDto> {
	
	private FlickrService flickrService;
	
	private ArticleService articleService;
	
	@Autowired
	public SaveArticlePhoto(FlickrService flickrService, ArticleService articleService) {
		this.flickrService = flickrService;
		this.articleService = articleService;
	}

	@Override
	public ArticleDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException{
		ArticleDto article = articleService.findById(id);
		String urlPhoto = flickrService.savePhoto(photo, titre);
		if (!StringUtils.hasLength(urlPhoto)) {
			throw new InvalidOperationException("Erreur lors de l'enregistrement de la photo de l'article", 
					ErrorCodes.UPDATE_PHOTO_EXCEPTION);
		}
		article.setPhoto(urlPhoto);
		return articleService.save(article);
	}

}
