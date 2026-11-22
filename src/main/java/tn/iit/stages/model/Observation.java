package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Observation ou recommandation formulee par l'enseignant encadrant
 * sur le deroulement du stage.
 */
@Entity
public class Observation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de l'observation est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateObservation;

    @NotBlank(message = "Le contenu est obligatoire")
    @Size(max = 2000)
    @Column(nullable = false, length = 2000)
    private String contenu;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_observation", nullable = false, length = 20)
    private TypeObservation type = TypeObservation.OBSERVATION;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;

    /** Enseignant auteur de l'observation (peut etre null). */
    @ManyToOne
    @JoinColumn(name = "auteur_id")
    private Enseignant auteur;

    public Observation() {
    }

    public Observation(LocalDate dateObservation, String contenu, TypeObservation type) {
        this.dateObservation = dateObservation;
        this.contenu = contenu;
        this.type = type == null ? TypeObservation.OBSERVATION : type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateObservation() {
        return dateObservation;
    }

    public void setDateObservation(LocalDate dateObservation) {
        this.dateObservation = dateObservation;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public TypeObservation getType() {
        return type;
    }

    public void setType(TypeObservation type) {
        this.type = type;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Enseignant getAuteur() {
        return auteur;
    }

    public void setAuteur(Enseignant auteur) {
        this.auteur = auteur;
    }
}
