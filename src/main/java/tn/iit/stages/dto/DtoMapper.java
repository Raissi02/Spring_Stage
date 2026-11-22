package tn.iit.stages.dto;

import tn.iit.stages.model.DeclarationAvancement;
import tn.iit.stages.model.EtapeProgression;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.Entreprise;
import tn.iit.stages.model.Livrable;
import tn.iit.stages.model.Observation;
import tn.iit.stages.model.Reunion;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.StatutEtape;

/**
 * Conversion entites JPA &lt;-&gt; DTO. Classe utilitaire sans etat.
 */
public final class DtoMapper {

    private DtoMapper() {
    }

    /* ------------------------- Etudiant ------------------------- */

    public static EtudiantDto toDto(Etudiant e) {
        if (e == null) {
            return null;
        }
        Enseignant encadrant = e.getEncadrant();
        return new EtudiantDto(
                e.getId(), e.getNom(), e.getPrenom(), e.getEmail(), e.getTelephone(),
                e.getNumeroEtudiant(), e.getFiliere(), e.getNiveau(),
                encadrant == null ? null : encadrant.getId(),
                encadrant == null ? null : encadrant.getNomComplet(),
                e.getStage() == null ? null : e.getStage().getId());
    }

    public static Etudiant toEntity(EtudiantDto d) {
        return new Etudiant(d.nom(), d.prenom(), d.email(), d.telephone(),
                d.numeroEtudiant(), d.filiere(), d.niveau());
    }

    /* ------------------------ Enseignant ------------------------ */

    public static EnseignantDto toDto(Enseignant e) {
        if (e == null) {
            return null;
        }
        long nombre = e.getEtudiants() == null ? 0 : e.getEtudiants().size();
        return new EnseignantDto(e.getId(), e.getNom(), e.getPrenom(), e.getEmail(),
                e.getTelephone(), e.getGrade(), e.getSpecialite(), nombre);
    }

    public static Enseignant toEntity(EnseignantDto d) {
        return new Enseignant(d.nom(), d.prenom(), d.email(), d.telephone(),
                d.grade(), d.specialite());
    }

    /* ------------------------- Entreprise ------------------------ */

    public static EntrepriseDto toDto(Entreprise e) {
        if (e == null) {
            return null;
        }
        int nombre = e.getStages() == null ? 0 : e.getStages().size();
        return new EntrepriseDto(e.getId(), e.getRaisonSociale(), e.getSecteur(),
                e.getContactNom(), e.getTelephone(), e.getEmail(), e.getAdresse(), nombre);
    }

    public static Entreprise toEntity(EntrepriseDto d) {
        return new Entreprise(d.raisonSociale(), d.secteur(), d.contactNom(),
                d.telephone(), d.email(), d.adresse());
    }

    /* --------------------------- Stage -------------------------- */

    public static StageDto toDto(Stage s) {
        if (s == null) {
            return null;
        }
        Entreprise entreprise = s.getEntreprise();
        Etudiant etudiant = s.getEtudiant();
        return new StageDto(
                s.getId(), s.getSujet(), s.getDescription(), s.getDateDebut(), s.getDateFin(),
                s.getStatut(),
                entreprise == null ? null : entreprise.getId(),
                entreprise == null ? null : entreprise.getRaisonSociale(),
                etudiant == null ? null : etudiant.getId(),
                etudiant == null ? null : etudiant.getNomComplet());
    }

    public static Stage toEntity(StageDto d) {
        return new Stage(d.sujet(), d.description(), d.dateDebut(), d.dateFin(), d.statut());
    }

    /* -------------------------- Reunion ------------------------- */

    public static ReunionDto toDto(Reunion r) {
        if (r == null) {
            return null;
        }
        return new ReunionDto(r.getId(), r.getDateReunion(), r.getObjet(), r.getCompteRendu(),
                r.getStage() == null ? null : r.getStage().getId());
    }

    public static Reunion toEntity(ReunionDto d) {
        return new Reunion(d.dateReunion(), d.objet(), d.compteRendu());
    }

    /* ------------------------- Observation ----------------------- */

    public static ObservationDto toDto(Observation o) {
        if (o == null) {
            return null;
        }
        Enseignant auteur = o.getAuteur();
        return new ObservationDto(o.getId(), o.getDateObservation(), o.getContenu(), o.getType(),
                o.getStage() == null ? null : o.getStage().getId(),
                auteur == null ? null : auteur.getId(),
                auteur == null ? null : auteur.getNomComplet());
    }

    public static Observation toEntity(ObservationDto d) {
        return new Observation(d.dateObservation(), d.contenu(), d.type());
    }

    /* ------------------------ Etape / Livrable ------------------- */

    public static EtapeProgressionDto toDto(EtapeProgression e) {
        if (e == null) {
            return null;
        }
        Enseignant validateur = e.getValideePar();
        return new EtapeProgressionDto(e.getId(), e.getTitre(), e.getDescription(), e.getOrdre(),
                e.getStatut(), e.getDateValidation(),
                e.getStage() == null ? null : e.getStage().getId(),
                validateur == null ? null : validateur.getId(),
                validateur == null ? null : validateur.getNomComplet());
    }

    public static EtapeProgression toEntity(EtapeProgressionDto d) {
        EtapeProgression etape = new EtapeProgression(d.titre(), d.description(), d.ordre());
        etape.setStatut(d.statut() == null ? StatutEtape.A_FAIRE : d.statut());
        return etape;
    }

    public static LivrableDto toDto(Livrable l) {
        if (l == null) {
            return null;
        }
        return new LivrableDto(l.getId(), l.getTitre(), l.getUrl(), l.getDescription(),
                l.getDateDepot(), l.getStage() == null ? null : l.getStage().getId());
    }

    public static Livrable toEntity(LivrableDto d) {
        return new Livrable(d.titre(), d.url(), d.description(), d.dateDepot());
    }

    /* ------------------- Declaration d'avancement ---------------- */

    public static DeclarationAvancementDto toDto(DeclarationAvancement d) {
        if (d == null) {
            return null;
        }
        return new DeclarationAvancementDto(d.getId(), d.getDateDeclaration(),
                d.getPourcentage(), d.getCommentaire(),
                d.getStage() == null ? null : d.getStage().getId());
    }

    public static DeclarationAvancement toEntity(DeclarationAvancementDto d) {
        return new DeclarationAvancement(d.dateDeclaration(), d.pourcentage(), d.commentaire());
    }
}
