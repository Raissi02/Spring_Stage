package tn.iit.stages.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.dto.StatistiquesDto;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.model.StatutEtape;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.EntrepriseRepository;
import tn.iit.stages.repository.EtapeProgressionRepository;
import tn.iit.stages.repository.EtudiantRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.StageService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class StageServiceImpl implements StageService {

    private final StageRepository stageRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final EtudiantRepository etudiantRepository;
    private final EnseignantRepository enseignantRepository;
    private final EtapeProgressionRepository etapeRepository;

    public StageServiceImpl(StageRepository stageRepository,
                            EntrepriseRepository entrepriseRepository,
                            EtudiantRepository etudiantRepository,
                            EnseignantRepository enseignantRepository,
                            EtapeProgressionRepository etapeRepository) {
        this.stageRepository = stageRepository;
        this.entrepriseRepository = entrepriseRepository;
        this.etudiantRepository = etudiantRepository;
        this.enseignantRepository = enseignantRepository;
        this.etapeRepository = etapeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stage> findAll() {
        return stageRepository.findAllByOrderByDateDebutDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stage> rechercher(String sujet) {
        if (sujet == null || sujet.isBlank()) {
            return findAll();
        }
        return stageRepository.findBySujetContainingIgnoreCaseOrderByDateDebutDesc(sujet.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stage> findByStatut(StatutStage statut) {
        if (statut == null) {
            return findAll();
        }
        return stageRepository.findByStatutOrderByDateDebutDesc(statut);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Stage> findByEntreprise(Long entrepriseId) {
        return stageRepository.findByEntrepriseIdOrderByDateDebutDesc(entrepriseId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Stage> findById(Long id) {
        return stageRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Stage getById(Long id) {
        return stageRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Stage", id));
    }

    @Override
    public Stage create(Stage stage, Long entrepriseId, Long etudiantId) {
        validerDates(stage);
        stage.setEntreprise(resoudreEntreprise(entrepriseId));
        affecterEtudiant(stage, etudiantId);
        return stageRepository.save(stage);
    }

    @Override
    public Stage update(Long id, Stage donnees, Long entrepriseId, Long etudiantId) {
        Stage existant = getById(id);
        validerDates(donnees);
        existant.setSujet(donnees.getSujet());
        existant.setDescription(donnees.getDescription());
        existant.setDateDebut(donnees.getDateDebut());
        existant.setDateFin(donnees.getDateFin());
        existant.setStatut(donnees.getStatut() == null ? StatutStage.EN_ATTENTE : donnees.getStatut());
        existant.setEntreprise(resoudreEntreprise(entrepriseId));
        affecterEtudiant(existant, etudiantId);
        return stageRepository.save(existant);
    }

    @Override
    public void delete(Long id) {
        Stage stage = getById(id);
        if (stage.getEtudiant() != null) {
            stage.getEtudiant().setStage(null);
        }
        stageRepository.delete(stage);
    }

    @Override
    @Transactional(readOnly = true)
    public StatistiquesDto statistiques() {
        Map<String, Long> parStatut = new LinkedHashMap<>();
        for (StatutStage statut : StatutStage.values()) {
            parStatut.put(statut.getLabel(), stageRepository.countByStatut(statut));
        }

        List<StatistiquesDto.StageParEntreprise> parEntreprise = new ArrayList<>();
        for (Object[] ligne : stageRepository.countStagesParEntreprise()) {
            parEntreprise.add(new StatistiquesDto.StageParEntreprise(
                    (String) ligne[0], ((Number) ligne[1]).longValue()));
        }

        return new StatistiquesDto(
                etudiantRepository.count(),
                enseignantRepository.count(),
                entrepriseRepository.count(),
                stageRepository.count(),
                etudiantRepository.countByEncadrantIsNull(),
                etapeRepository.count(),
                etapeRepository.countByStatut(StatutEtape.VALIDEE),
                parStatut,
                parEntreprise);
    }

    /* ----------------------- helpers prives ----------------------- */

    private void validerDates(Stage stage) {
        LocalDate debut = stage.getDateDebut();
        LocalDate fin = stage.getDateFin();
        if (debut != null && fin != null && !fin.isAfter(debut)) {
            throw new BusinessException(
                    "La date de fin doit etre posterieure a la date de debut.");
        }
    }

    private tn.iit.stages.model.Entreprise resoudreEntreprise(Long entrepriseId) {
        if (entrepriseId == null) {
            throw new BusinessException("L'entreprise d'accueil est obligatoire.");
        }
        return entrepriseRepository.findById(entrepriseId)
                .orElseThrow(() -> ResourceNotFoundException.of("Entreprise", entrepriseId));
    }

    private void affecterEtudiant(Stage stage, Long etudiantId) {
        if (etudiantId == null) {
            if (stage.getEtudiant() != null) {
                stage.getEtudiant().setStage(null);
            }
            stage.setEtudiant(null);
            return;
        }

        Etudiant etudiant = etudiantRepository.findById(etudiantId)
                .orElseThrow(() -> ResourceNotFoundException.of("Etudiant", etudiantId));

        stageRepository.findFirstByEtudiantId(etudiantId).ifPresent(autre -> {
            if (stage.getId() == null || !autre.getId().equals(stage.getId())) {
                throw new BusinessException("Cet etudiant est deja affecte a un autre stage.");
            }
        });

        if (stage.getEtudiant() != null && !stage.getEtudiant().getId().equals(etudiantId)) {
            stage.getEtudiant().setStage(null);
        }
        stage.setEtudiant(etudiant);
        etudiant.setStage(stage);
    }
}
