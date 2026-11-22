package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Size;

/**
 * Etudiant stagiaire.
 *
 * <ul>
 *   <li>{@code encadrant} : enseignant affecte par l'administrateur pedagogique
 *       (relation N-1).</li>
 *   <li>{@code stage} : stage courant de l'etudiant (relation 1-1, cote inverse).</li>
 * </ul>
 */
@Entity
public class Etudiant extends Personne {

    @Size(max = 30)
    @Column(unique = true, length = 30)
    private String numeroEtudiant;

    @Size(max = 60)
    @Column(length = 60)
    private String filiere;

    @Size(max = 30)
    @Column(length = 30)
    private String niveau;

    @ManyToOne
    @JoinColumn(name = "encadrant_id")
    private Enseignant encadrant;

    @OneToOne(mappedBy = "etudiant")
    private Stage stage;

    public Etudiant() {
    }

    public Etudiant(String nom, String prenom, String email, String telephone,
                    String numeroEtudiant, String filiere, String niveau) {
        super(nom, prenom, email, telephone);
        this.numeroEtudiant = numeroEtudiant;
        this.filiere = filiere;
        this.niveau = niveau;
    }

    public String getNumeroEtudiant() {
        return numeroEtudiant;
    }

    public void setNumeroEtudiant(String numeroEtudiant) {
        this.numeroEtudiant = numeroEtudiant;
    }

    public String getFiliere() {
        return filiere;
    }

    public void setFiliere(String filiere) {
        this.filiere = filiere;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public Enseignant getEncadrant() {
        return encadrant;
    }

    public void setEncadrant(Enseignant encadrant) {
        this.encadrant = encadrant;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
