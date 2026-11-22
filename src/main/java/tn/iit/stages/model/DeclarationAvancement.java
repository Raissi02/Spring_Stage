package tn.iit.stages.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Declaration d'avancement saisie par l'etudiant stagiaire
 * ("declarer l'avancement de ses travaux").
 */
@Entity
public class DeclarationAvancement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDeclaration;

    @Min(value = 0, message = "Le pourcentage doit etre compris entre 0 et 100")
    @Max(value = 100, message = "Le pourcentage doit etre compris entre 0 et 100")
    @Column(nullable = false)
    private int pourcentage;

    @Size(max = 1000)
    @Column(length = 1000)
    private String commentaire;

    @ManyToOne(optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;

    public DeclarationAvancement() {
    }

    public DeclarationAvancement(LocalDate dateDeclaration, int pourcentage, String commentaire) {
        this.dateDeclaration = dateDeclaration;
        this.pourcentage = pourcentage;
        this.commentaire = commentaire;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDeclaration() {
        return dateDeclaration;
    }

    public void setDateDeclaration(LocalDate dateDeclaration) {
        this.dateDeclaration = dateDeclaration;
    }

    public int getPourcentage() {
        return pourcentage;
    }

    public void setPourcentage(int pourcentage) {
        this.pourcentage = pourcentage;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
