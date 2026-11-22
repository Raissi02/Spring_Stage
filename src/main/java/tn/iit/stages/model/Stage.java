package tn.iit.stages.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Stage effectue par un etudiant dans une entreprise d'accueil.
 *
 * <p>Relations :</p>
 * <ul>
 *   <li>N-1 vers {@link Entreprise} ;</li>
 *   <li>1-1 vers {@link Etudiant} (cote maitre : porte la cle etrangere) ;</li>
 *   <li>1-N vers {@link Reunion}, {@link Observation}, {@link EtapeProgression}
 *       et {@link Livrable} (cascade totale + suppression orpheline).</li>
 * </ul>
 */
@Entity
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le sujet est obligatoire")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String sujet;

    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    @NotNull(message = "La date de debut est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateDebut;

    @NotNull(message = "La date de fin est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate dateFin;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatutStage statut = StatutStage.EN_ATTENTE;

    @ManyToOne(optional = false)
    @JoinColumn(name = "entreprise_id", nullable = false)
    private Entreprise entreprise;

    @OneToOne
    @JoinColumn(name = "etudiant_id", unique = true)
    private Etudiant etudiant;

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateReunion DESC")
    private List<Reunion> reunions = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateObservation DESC")
    private List<Observation> observations = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ordre ASC")
    private List<EtapeProgression> etapes = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateDepot DESC")
    private List<Livrable> livrables = new ArrayList<>();

    @OneToMany(mappedBy = "stage", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dateDeclaration DESC")
    private List<DeclarationAvancement> declarations = new ArrayList<>();

    public Stage() {
    }

    public Stage(String sujet, String description, LocalDate dateDebut,
                 LocalDate dateFin, StatutStage statut) {
        this.sujet = sujet;
        this.description = description;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut == null ? StatutStage.EN_ATTENTE : statut;
    }

    /* ---- Synchronisation des relations bidirectionnelles ---- */

    public void addReunion(Reunion reunion) {
        reunions.add(reunion);
        reunion.setStage(this);
    }

    public void addObservation(Observation observation) {
        observations.add(observation);
        observation.setStage(this);
    }

    public void addEtape(EtapeProgression etape) {
        etapes.add(etape);
        etape.setStage(this);
    }

    public void addLivrable(Livrable livrable) {
        livrables.add(livrable);
        livrable.setStage(this);
    }

    public void addDeclaration(DeclarationAvancement declaration) {
        declarations.add(declaration);
        declaration.setStage(this);
    }

    /** Duree du stage en jours (propriete derivee pour l'affichage). */
    public long getDureeEnJours() {
        if (dateDebut == null || dateFin == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dateDebut, dateFin);
    }

    /** Pourcentage d'etapes validees (propriete derivee pour l'affichage). */
    public int getAvancement() {
        if (etapes == null || etapes.isEmpty()) {
            return 0;
        }
        long validees = etapes.stream()
                .filter(e -> e.getStatut() == StatutEtape.VALIDEE)
                .count();
        return (int) Math.round(100.0 * validees / etapes.size());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSujet() {
        return sujet;
    }

    public void setSujet(String sujet) {
        this.sujet = sujet;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public StatutStage getStatut() {
        return statut;
    }

    public void setStatut(StatutStage statut) {
        this.statut = statut;
    }

    public Entreprise getEntreprise() {
        return entreprise;
    }

    public void setEntreprise(Entreprise entreprise) {
        this.entreprise = entreprise;
    }

    public Etudiant getEtudiant() {
        return etudiant;
    }

    public void setEtudiant(Etudiant etudiant) {
        this.etudiant = etudiant;
    }

    public List<Reunion> getReunions() {
        return reunions;
    }

    public void setReunions(List<Reunion> reunions) {
        this.reunions = reunions;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public List<EtapeProgression> getEtapes() {
        return etapes;
    }

    public void setEtapes(List<EtapeProgression> etapes) {
        this.etapes = etapes;
    }

    public List<Livrable> getLivrables() {
        return livrables;
    }

    public void setLivrables(List<Livrable> livrables) {
        this.livrables = livrables;
    }

    public List<DeclarationAvancement> getDeclarations() {
        return declarations;
    }

    public void setDeclarations(List<DeclarationAvancement> declarations) {
        this.declarations = declarations;
    }
}
