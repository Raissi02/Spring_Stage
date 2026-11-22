package tn.iit.stages.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.Observation;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.ObservationRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.ObservationService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class ObservationServiceImpl implements ObservationService {

    private final ObservationRepository observationRepository;
    private final StageRepository stageRepository;
    private final EnseignantRepository enseignantRepository;

    public ObservationServiceImpl(ObservationRepository observationRepository,
                                  StageRepository stageRepository,
                                  EnseignantRepository enseignantRepository) {
        this.observationRepository = observationRepository;
        this.stageRepository = stageRepository;
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Observation> findByStage(Long stageId) {
        return observationRepository.findByStageIdOrderByDateObservationDesc(stageId);
    }

    @Override
    public Observation add(Long stageId, Long auteurId, Observation observation) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stage", stageId));
        if (observation.getDateObservation() == null) {
            observation.setDateObservation(LocalDate.now());
        }
        if (auteurId != null) {
            Enseignant auteur = enseignantRepository.findById(auteurId)
                    .orElseThrow(() -> ResourceNotFoundException.of("Enseignant", auteurId));
            observation.setAuteur(auteur);
        }
        stage.addObservation(observation);
        return observationRepository.save(observation);
    }

    @Override
    public void delete(Long id) {
        Observation observation = observationRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Observation", id));
        observationRepository.delete(observation);
    }
}
