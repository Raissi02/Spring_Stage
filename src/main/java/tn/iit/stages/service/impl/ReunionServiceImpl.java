package tn.iit.stages.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Reunion;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.ReunionRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.ReunionService;

import java.util.List;

@Service
@Transactional
public class ReunionServiceImpl implements ReunionService {

    private final ReunionRepository reunionRepository;
    private final StageRepository stageRepository;

    public ReunionServiceImpl(ReunionRepository reunionRepository, StageRepository stageRepository) {
        this.reunionRepository = reunionRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Reunion> findByStage(Long stageId) {
        return reunionRepository.findByStageIdOrderByDateReunionDesc(stageId);
    }

    @Override
    public Reunion add(Long stageId, Reunion reunion) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stage", stageId));
        stage.addReunion(reunion);
        return reunionRepository.save(reunion);
    }

    @Override
    public Reunion update(Long id, Reunion donnees) {
        Reunion existante = reunionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Reunion", id));
        existante.setDateReunion(donnees.getDateReunion());
        existante.setObjet(donnees.getObjet());
        existante.setCompteRendu(donnees.getCompteRendu());
        return reunionRepository.save(existante);
    }

    @Override
    public void delete(Long id) {
        Reunion reunion = reunionRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Reunion", id));
        reunionRepository.delete(reunion);
    }
}
