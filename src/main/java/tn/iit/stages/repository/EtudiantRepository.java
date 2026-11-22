package tn.iit.stages.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.iit.stages.model.Etudiant;

import java.util.List;
import java.util.Optional;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    List<Etudiant> findByEncadrantId(Long enseignantId);

    boolean existsByEncadrantId(Long enseignantId);

    Optional<Etudiant> findByEmail(String email);

    /**
     * Recherche multi-criteres derivee du nom de la methode
     * (nom, prenom ou numero etudiant).
     */
    List<Etudiant> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroEtudiantContainingIgnoreCase(
            String nom, String prenom, String numeroEtudiant);

    long countByEncadrantIsNull();
}
