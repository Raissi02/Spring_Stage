package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Enseignant encadrant : consulte ses etudiants affectes, gere les reunions,
 * ajoute des observations et valide les etapes de progression.
 */
@Entity
public class Enseignant extends Personne {

    @Size(max = 50)
    @Column(length = 50)
    private String grade;

    @Size(max = 60)
    @Column(length = 60)
    private String specialite;

    @OneToMany(mappedBy = "encadrant")
    private List<Etudiant> etudiants = new ArrayList<>();

    public Enseignant() {
    }

    public Enseignant(String nom, String prenom, String email, String telephone,
                      String grade, String specialite) {
        super(nom, prenom, email, telephone);
        this.grade = grade;
        this.specialite = specialite;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    public List<Etudiant> getEtudiants() {
        return etudiants;
    }

    public void setEtudiants(List<Etudiant> etudiants) {
        this.etudiants = etudiants;
    }
}
