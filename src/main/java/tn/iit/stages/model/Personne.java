package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Classe mere des acteurs personnes (etudiant, enseignant).
 *
 * <p>Strategie d'heritage JPA : {@link MappedSuperclass}. Les colonnes
 * communes sont dupliquees dans chaque table fille (etudiant, enseignant),
 * sans table "personne" intermediaire.</p>
 */
@MappedSuperclass
public abstract class Personne {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 50, message = "Le nom ne doit pas depasser 50 caracteres")
    @Column(nullable = false, length = 50)
    private String nom;

    @NotBlank(message = "Le prenom est obligatoire")
    @Size(max = 50, message = "Le prenom ne doit pas depasser 50 caracteres")
    @Column(nullable = false, length = 50)
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    @Size(max = 100)
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Size(max = 20)
    @Column(length = 20)
    private String telephone;

    protected Personne() {
    }

    protected Personne(String nom, String prenom, String email, String telephone) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
    }

    /** Propriete derivee, pratique pour l'affichage (Thymeleaf / DTO). */
    public String getNomComplet() {
        return (prenom == null ? "" : prenom) + " " + (nom == null ? "" : nom);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
