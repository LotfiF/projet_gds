package fr.esic.gestiondestock.services.strategy;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.flickr4java.flickr.FlickrException;

import fr.esic.gestiondestock.dto.UtilisateurDto;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.services.FlickrService;
import fr.esic.gestiondestock.services.UtilisateurService;
import lombok.extern.slf4j.Slf4j;

@Service("utilisateurStrategy")
@Slf4j
public class SaveUtilisateurPhoto implements Strategy<UtilisateurDto> {
	
	private FlickrService flickrService;
	
	private UtilisateurService utilisateurService;
	
	@Autowired
	public SaveUtilisateurPhoto(FlickrService flickrService, UtilisateurService utilisateurService) {
		this.flickrService = flickrService;
		this.utilisateurService = utilisateurService;
	}

	@Override
	public UtilisateurDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException {
		UtilisateurDto utilisateur = utilisateurService.findById(id);
		String urlPhoto = flickrService.savePhoto(photo, titre);
		if (!StringUtils.hasLength(urlPhoto)) {
			throw new InvalidOperationException("Erreur lors de l'enregistrement de la photo", 
					ErrorCodes.UPDATE_PHOTO_EXCEPTION);
		}
		utilisateur.setPhoto(urlPhoto);
		return utilisateurService.save(utilisateur);
	}

}
