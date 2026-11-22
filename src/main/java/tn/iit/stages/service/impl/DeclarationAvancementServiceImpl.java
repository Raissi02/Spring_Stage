package tn.iit.stages.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.model.DeclarationAvancement;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.DeclarationAvancementRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.DeclarationAvancementService;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class DeclarationAvancementServiceImpl implements DeclarationAvancementService {

    private final DeclarationAvancementRepository declarationRepository;
    private final StageRepository stageRepository;

    public DeclarationAvancementServiceImpl(DeclarationAvancementRepository declarationRepository,
                                            StageRepository stageRepository) {
        this.declarationRepository = declarationRepository;
        this.stageRepository = stageRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeclarationAvancement> findByStage(Long stageId) {
        return declarationRepository.findByStageIdOrderByDateDeclarationDesc(stageId);
    }

    @Override
    public DeclarationAvancement add(Long stageId, DeclarationAvancement declaration) {
        if (declaration.getPourcentage() < 0 || declaration.getPourcentage() > 100) {
            throw new BusinessException(
                    "Le pourcentage d'avancement doit etre compris entre 0 et 100.");
        }
        Stage stage = stageRepository.findById(stageId)
                .orElseThrow(() -> ResourceNotFoundException.of("Stage", stageId));
        if (declaration.getDateDeclaration() == null) {
            declaration.setDateDeclaration(LocalDate.now());
        }
        stage.addDeclaration(declaration);
        return declarationRepository.save(declaration);
    }

    @Override
    public void delete(Long id) {
        DeclarationAvancement declaration = declarationRepository.findById(id)
                .orElseThrow(() -> ResourceNotFoundException.of("DeclarationAvancement", id));
        declarationRepository.delete(declaration);
    }
}
