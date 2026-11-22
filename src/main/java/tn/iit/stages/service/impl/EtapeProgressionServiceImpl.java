package tn.iit.stages.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.EtapeProgression;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.StatutEtape;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.EtapeProgressionRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.EtapeProgressionService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class EtapeProgressionServiceImpl implements EtapeProgressionService {

    private final EtapeProgressionRepository etapeRepository;
    private final StageRepository stageRepository;
    private final EnseignantRepository enseignantRepository;

    public EtapeProgressionServiceImpl(EtapeProgressionRepository etapeRepository,
                                       StageRepository stageRepository,
                                       EnseignantRepository enseignantRepository) {
        this.etapeRepository = etapeRepository;
        this.stageRepository = stageRepository;
        this.enseignantRepository = enseignantRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EtapeProgression> findByStage(Long stageId) {
        return etapeRepository.findByStageIdOrderByOrdreAsc(stageId);
    }

    @Override
    public EtapeProgression add(Long stageId, EtapeProgression etape) {
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stage", stageId));
        if (etape.getOrdre() <= 0) {
            etape.setOrdre(1);
        }
        if (etape.getStatut() == null) {
            etape.setStatut(StatutEtape.A_FAIRE);
        }
        stage.addEtape(etape);
        return etapeRepository.save(etape);
    }

    @Override
    public EtapeProgression valider(Long etapeId, Long enseignantId) {
        EtapeProgression etape = etapeRepository.findById(etapeId)
                .orElseThrow(() -> ResourceNotFoundException.of("Etape", etapeId));
        if (etape.getStatut() == StatutEtape.VALIDEE) {
            return etape;
        }
        if (enseignantId == null) {
            throw new BusinessException("Un enseignant est necessaire pour valider une etape.");
        }
        Enseignant enseignant = enseignantRepository.findById(enseignantId)
                .orElseThrow(() -> ResourceNotFoundException.of("Enseignant", enseignantId));
        etape.setStatut(StatutEtape.VALIDEE);
        etape.setDateValidation(LocalDate.now());
        etape.setValideePar(enseignant);
        return etapeRepository.save(etape);
    }

    @Override
    public void delete(Long id) {
        EtapeProgression etape = etapeRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("Etape", id));
        etapeRepository.delete(etape);
    }
}
