package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
 * Reunion de suivi organisee par l'enseignant encadrant.
 */
@Entity
public class Reunion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La date de la reunion est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateReunion;

    @NotBlank(message = "L'objet de la reunion est obligatoire")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String objet;

    @Size(max = 2000)
    @Column(length = 2000)
    private String compteRendu;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;

    public Reunion() {
    }

    public Reunion(LocalDate dateReunion, String objet, String compteRendu) {
        this.dateReunion = dateReunion;
        this.objet = objet;
        this.compteRendu = compteRendu;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateReunion() {
        return dateReunion;
    }

    public void setDateReunion(LocalDate dateReunion) {
        this.dateReunion = dateReunion;
    }

    public String getObjet() {
        return objet;
    }

    public void setObjet(String objet) {
        this.objet = objet;
    }

    public String getCompteRendu() {
        return compteRendu;
    }

    public void setCompteRendu(String compteRendu) {
        this.compteRendu = compteRendu;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
