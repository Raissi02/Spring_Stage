package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Entreprise d'accueil des stagiaires.
 */
@Entity
public class Entreprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La raison sociale est obligatoire")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String raisonSociale;

    @Size(max = 80)
    @Column(length = 80)
    private String secteur;

    @Size(max = 80)
    @Column(length = 80)
    private String contactNom;

    @Size(max = 20)
    @Column(length = 20)
    private String telephone;

    @Email(message = "Format d'email invalide")
    @Size(max = 100)
    @Column(length = 100)
    private String email;

    @Size(max = 150)
    @Column(length = 150)
    private String adresse;

    @OneToMany(mappedBy = "entreprise")
    private List<Stage> stages = new ArrayList<>();

    public Entreprise() {
    }

    public Entreprise(String raisonSociale, String secteur, String contactNom,
                      String telephone, String email, String adresse) {
        this.raisonSociale = raisonSociale;
        this.secteur = secteur;
        this.contactNom = contactNom;
        this.telephone = telephone;
        this.email = email;
        this.adresse = adresse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRaisonSociale() {
        return raisonSociale;
    }

    public void setRaisonSociale(String raisonSociale) {
        this.raisonSociale = raisonSociale;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }

    public String getContactNom() {
        return contactNom;
    }

    public void setContactNom(String contactNom) {
        this.contactNom = contactNom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public List<Stage> getStages() {
        return stages;
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public int getNombreStages() {
        return stages.size();
    }
}
