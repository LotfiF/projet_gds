package fr.esic.gestiondestock.dto;

import fr.esic.gestiondestock.model.Vente;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class VenteDto {

    private Integer id;

    private String code;

    private Instant dateVente;

    private String commentaire;
    
    private Integer idEntreprise;
    
    private List<LigneVenteDto> ligneVente;

    public static VenteDto fromEntity(Vente vente) {
        if (vente == null) {
            return null;
        }
        return VenteDto.builder()
                .id(vente.getId())
                .code(vente.getCode())
                .commentaire(vente.getCommentaire())
                .dateVente(vente.getDateVente())
                .idEntreprise(vente.getIdEntreprise())
                .build();
    }

    public static Vente toEntity(VenteDto venteDto) {
        if (venteDto == null) {
            return null;
        }
        Vente vente = new Vente();
        vente.setId(venteDto.getId());
        vente.setCode(venteDto.getCode());
        vente.setCommentaire(venteDto.getCommentaire());
        vente.setDateVente(venteDto.getDateVente());
        vente.setIdEntreprise(venteDto.getIdEntreprise());
        return vente;
    }
}
