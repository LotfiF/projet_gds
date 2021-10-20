package fr.esic.gestiondestock.services.strategy;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.flickr4java.flickr.FlickrException;

import fr.esic.gestiondestock.dto.FournisseurDto;
import fr.esic.gestiondestock.exception.ErrorCodes;
import fr.esic.gestiondestock.exception.InvalidOperationException;
import fr.esic.gestiondestock.services.FlickrService;
import fr.esic.gestiondestock.services.FournisseurService;
import lombok.extern.slf4j.Slf4j;

@Service("fournisseurStrategy")
@Slf4j
public class SaveFournisseurPhoto implements Strategy<FournisseurDto> {
	
	
	private FlickrService flickrService;
	
	private FournisseurService fournisseurService;
	
	@Autowired
	public SaveFournisseurPhoto(FlickrService flickrService, FournisseurService fournisseurService) {
		this.flickrService = flickrService;
		this.fournisseurService = fournisseurService;
	}
	
	@Override
	public FournisseurDto savePhoto(Integer id, InputStream photo, String titre) throws FlickrException {
		FournisseurDto fournisseur = fournisseurService.findById(id);
		String urlPhoto = flickrService.savePhoto(photo, titre);
		if (!StringUtils.hasLength(urlPhoto)) {
			throw new InvalidOperationException("Erreur lors de l'enregistrement de la photo", 
					ErrorCodes.UPDATE_PHOTO_EXCEPTION);
		}
		fournisseur.setPhoto(urlPhoto);
		return fournisseurService.save(fournisseur);
	}

}
