package tn.iit.stages.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.EtudiantRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.EtudiantService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EtudiantServiceImpl implements EtudiantService {

    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final StageRepository stageRepository;

    public EtudiantServiceImpl(EtudiantRepository etudiantRepository,
                               EnseignantRepository enseignantRepository,
                               StageRepository stageRepository) {
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Etudiant> findAll() {
        return etudiantRepository.findAll(
                Sort.by(Sort.Order.asc("nom"), Sort.Order.asc("prenom")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Etudiant> rechercher(String motCle) {
        if (motCle == null || motCle.isBlank()) {
            return findAll();
        }
        String mc = motCle.trim();
        return etudiantRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrNumeroEtudiantContainingIgnoreCase(
                        mc, mc, mc);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Etudiant> findById(Long id) {
        return etudiantRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Etudiant getById(Long id) {
        return etudiantRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Etudiant", id));
    }

    @Override
    public Etudiant create(Etudiant etudiant, Long encadrantId) {
        verifierEmailDisponible(etudiant.getEmail(), null);
        etudiant.setEncadrant(resoudreEncadrant(encadrantId));
        return etudiantRepository.save(etudiant);
    }

    @Override
    public Etudiant update(Long id, Etudiant donnees, Long encadrantId) {
        Etudiant existant = getById(id);
        verifierEmailDisponible(donnees.getEmail(), id);
        copier(donnees, existant);
        existant.setEncadrant(resoudreEncadrant(encadrantId));
        return etudiantRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        Etudiant etudiant = getById(id);
        if (stageRepository.existsByEtudiantId(id)) {
            throw new BusinessException(
                    "Suppression impossible : cet etudiant possede un stage. Supprimez d'abord le stage.");
        }
        etudiantRepository.delete(etudiant);
    }

    @Override
    public Etudiant affecterEncadrant(Long etudiantId, Long enseignantId) {
        Etudiant etudiant = getById(etudiantId);
        etudiant.setEncadrant(resoudreEncadrant(enseignantId));
        return etudiantRepository.save(etudiant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Etudiant> findByEncadrant(Long enseignantId) {
        return etudiantRepository.findByEncadrantId(enseignantId);
    }

    /* ----------------------- helpers prives ----------------------- */

    private Enseignant resoudreEncadrant(Long encadrantId) {
        if (encadrantId == null) {
            return null;
        }
        return enseignantRepository.findById(encadrantId)
                .orElseThrow(() -> ResourceNotFoundException.of("Enseignant", encadrantId));
    }

    private void verifierEmailDisponible(String email, Long idIgnore) {
        etudiantRepository.findByEmail(email).ifPresent(autre -> {
            if (idIgnore == null || !autre.getId().equals(idIgnore)) {
                throw new BusinessException("Cet email est deja utilise par un etudiant.");
            }
        });
        if (enseignantRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("Cet email est deja utilise par un enseignant.");
        }
    }

    private void copier(Etudiant source, Etudiant cible) {
        cible.setNom(source.getNom());
        cible.setPrenom(source.getPrenom());
        cible.setEmail(source.getEmail());
        cible.setTelephone(source.getTelephone());
        cible.setNumeroEtudiant(source.getNumeroEtudiant());
        cible.setFiliere(source.getFiliere());
        cible.setNiveau(source.getNiveau());
    }
}
