package fr.esic.gestiondestock.model;

import lombok.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.esic.gestiondestock.dto.RolesDto;
import fr.esic.gestiondestock.dto.UtilisateurDto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "utilisateur")
public class Utilisateur extends AbstractEntity{

    @Column(name ="nom")
    private String nom;

    @Column(name ="prenom")
    private String prenom;

    @Column(name ="mail")
    private String mail;

    @Column(name ="motdepasse")
    private String motDePasse;

    @Column(name = "datedenaissance")
    private LocalDateTime dateDeNaissance;
  //private Instant dateDeNaissance;

    @Embedded
    private Adresse adresse;

    @Column(name ="photo")
    private String photo;

//    @ManyToOne(cascade = CascadeType.MERGE)
    @ManyToOne
    @JoinColumn(name= "identreprise")
    private Entreprise entreprise;

//    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
//    private List<Roles> roles;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "utilisateur")
    @JsonIgnore
    private List<Roles> roles;
}
