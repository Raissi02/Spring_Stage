package tn.iit.stages.service;

import tn.iit.stages.model.Etudiant;

import java.util.List;
import java.util.Optional;

public interface EtudiantService {

    List<Etudiant> findAll();

    List<Etudiant> rechercher(String motCle);

    Optional<Etudiant> findById(Long id);

    Etudiant getById(Long id);

    Etudiant create(Etudiant etudiant, Long encadrantId);

    Etudiant update(Long id, Etudiant etudiant, Long encadrantId);

    void delete(Long id);

    /** Affecte (ou reaffecte) un enseignant encadrant a un etudiant. */
    Etudiant affecterEncadrant(Long etudiantId, Long enseignantId);

    List<Etudiant> findByEncadrant(Long enseignantId);
}
