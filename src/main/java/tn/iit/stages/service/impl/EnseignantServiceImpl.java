package tn.iit.stages.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.EtapeProgressionRepository;
import tn.iit.stages.repository.EtudiantRepository;
import tn.iit.stages.repository.ObservationRepository;
import tn.iit.stages.service.EnseignantService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;
    private final EtudiantRepository etudiantRepository;
    private final ObservationRepository observationRepository;
    private final EtapeProgressionRepository etapeRepository;

    public EnseignantServiceImpl(EnseignantRepository enseignantRepository,
                                 EtudiantRepository etudiantRepository,
                                 ObservationRepository observationRepository,
                                 EtapeProgressionRepository etapeRepository) {
        this.enseignantRepository = enseignantRepository;
        this.etudiantRepository = etudiantRepository;
        this.observationRepository = observationRepository;
        this.etapeRepository = etapeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enseignant> findAll() {
        return enseignantRepository.findAll(
                Sort.by(Sort.Order.asc("nom"), Sort.Order.asc("prenom")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enseignant> rechercher(String motCle) {
        if (motCle == null || motCle.isBlank()) {
            return findAll();
        }
        String mc = motCle.trim();
        return enseignantRepository
                .findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCaseOrSpecialiteContainingIgnoreCase(
                        mc, mc, mc);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enseignant> findById(Long id) {
        return enseignantRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Enseignant getById(Long id) {
        return enseignantRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Enseignant", id));
    }

    @Override
    public Enseignant create(Enseignant enseignant) {
        verifierEmailDisponible(enseignant.getEmail(), null);
        return enseignantRepository.save(enseignant);
    }

    @Override
    public Enseignant update(Long id, Enseignant donnees) {
        Enseignant existant = getById(id);
        verifierEmailDisponible(donnees.getEmail(), id);
        existant.setNom(donnees.getNom());
        existant.setPrenom(donnees.getPrenom());
        existant.setEmail(donnees.getEmail());
        existant.setTelephone(donnees.getTelephone());
        existant.setGrade(donnees.getGrade());
        existant.setSpecialite(donnees.getSpecialite());
        return enseignantRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        Enseignant enseignant = getById(id);
        if (etudiantRepository.existsByEncadrantId(id)) {
            throw new BusinessException(
                    "Suppression impossible : cet enseignant encadre encore des etudiants.");
        }
        if (observationRepository.existsByAuteurId(id)) {
            throw new BusinessException(
                    "Suppression impossible : cet enseignant a redige des observations.");
        }
        if (etapeRepository.existsByValideeParId(id)) {
            throw new BusinessException(
                    "Suppression impossible : cet enseignant a valide des etapes.");
        }
        enseignantRepository.delete(enseignant);
    }

    /* ----------------------- helpers prives ----------------------- */

    private void verifierEmailDisponible(String email, Long idIgnore) {
        enseignantRepository.findByEmail(email).ifPresent(autre -> {
            if (idIgnore == null || !autre.getId().equals(idIgnore)) {
                throw new BusinessException("Cet email est deja utilise par un enseignant.");
            }
        });
        if (etudiantRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("Cet email est deja utilise par un etudiant.");
        }
    }
}
