package tn.iit.stages.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Livrable;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.LivrableRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.LivrableService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class LivrableServiceImpl implements LivrableService {

    private final LivrableRepository livrableRepository;
    private final StageRepository stageRepository;

    public LivrableServiceImpl(LivrableRepository livrableRepository, StageRepository stageRepository) {
        this.livrableRepository = livrableRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Livrable> findByStage(Long stageId) {
        return livrableRepository.findByStageIdOrderByDateDepotDesc(stageId);
    }

    @Override
    public Livrable add(Long stageId, Livrable livrable) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stage", stageId));
        if (livrable.getDateDepot() == null) {
            livrable.setDateDepot(LocalDate.now());
        }
        stage.addLivrable(livrable);
        return livrableRepository.save(livrable);
    }

    @Override
    public void delete(Long id) {
        Livrable livrable = livrableRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Livrable", id));
        livrableRepository.delete(livrable);
    }
}
