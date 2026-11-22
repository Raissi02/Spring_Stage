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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Etape de progression du stage (ex. cadrage, developpement, rapport, soutenance).
 * L'enseignant encadrant valide chaque etape.
 */
@Entity
public class EtapeProgression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le titre est obligatoire")
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String titre;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @Min(value = 1, message = "L'ordre doit etre superieur ou egal a 1")
    @Column(nullable = false)
    private int ordre = 1;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutEtape statut = StatutEtape.A_FAIRE;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateValidation;

    @ManyToOne
    @JoinColumn(name = "validee_par_id")
    private Enseignant valideePar;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;

    public EtapeProgression() {
    }

    public EtapeProgression(String titre, String description, int ordre) {
        this.titre = titre;
        this.description = description;
        this.ordre = ordre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getOrdre() {
        return ordre;
    }

    public void setOrdre(int ordre) {
        this.ordre = ordre;
    }

    public StatutEtape getStatut() {
        return statut;
    }

    public void setStatut(StatutEtape statut) {
        this.statut = statut;
    }

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public Enseignant getValideePar() {
        return valideePar;
    }

    public void setValideePar(Enseignant valideePar) {
        this.valideePar = valideePar;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
