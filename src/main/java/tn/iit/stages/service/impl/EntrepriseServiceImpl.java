package tn.iit.stages.service.impl;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Entreprise;
import tn.iit.stages.repository.EntrepriseRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.EntrepriseService;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EntrepriseServiceImpl implements EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;
    private final StageRepository stageRepository;

    public EntrepriseServiceImpl(EntrepriseRepository entrepriseRepository,
                                 StageRepository stageRepository) {
        this.entrepriseRepository = entrepriseRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entreprise> findAll() {
        return entrepriseRepository.findAll(Sort.by(Sort.Order.asc("raisonSociale")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Entreprise> rechercher(String motCle) {
        if (motCle == null || motCle.isBlank()) {
            return findAll();
        }
        return entrepriseRepository.findByRaisonSocialeContainingIgnoreCase(motCle.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Entreprise> findById(Long id) {
        return entrepriseRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Entreprise getById(Long id) {
        return entrepriseRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Entreprise", id));
    }

    @Override
    public Entreprise create(Entreprise entreprise) {
        return entrepriseRepository.save(entreprise);
    }

    @Override
    public Entreprise update(Long id, Entreprise donnees) {
        Entreprise existante = getById(id);
        existante.setRaisonSociale(donnees.getRaisonSociale());
        existante.setSecteur(donnees.getSecteur());
        existante.setContactNom(donnees.getContactNom());
        existante.setTelephone(donnees.getTelephone());
        existante.setEmail(donnees.getEmail());
        existante.setAdresse(donnees.getAdresse());
        return entrepriseRepository.save(existante);
    }

    @Override
    public void delete(Long id) {
        Entreprise entreprise = getById(id);
        if (stageRepository.existsByEntrepriseId(id)) {
            throw new BusinessException(
                    "Suppression impossible : des stages sont associes a cette entreprise.");
        }
        entrepriseRepository.delete(entreprise);
    }
}
