package tn.iit.stages.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.model.DeclarationAvancement;
import tn.iit.stages.model.Stage;
import tn.iit.stages.model.StatutStage;
import tn.iit.stages.repository.DeclarationAvancementRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.impl.DeclarationAvancementServiceImpl;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeclarationAvancementServiceTest {

    @Mock
    private DeclarationAvancementRepository declarationRepository;

    @Mock
    private StageRepository stageRepository;

    @InjectMocks
    private DeclarationAvancementServiceImpl declarationService;

    @Test
    void ajouteUneDeclarationEtCompleteLaDate() {
        Stage stage = new Stage("Sujet", "Description",
                LocalDate.of(2026, 1, 5), LocalDate.of(2026, 6, 5), StatutStage.EN_COURS);
        stage.setId(7L);

        when(stageRepository.findById(7L)).thenReturn(Optional.of(stage));
        when(declarationRepository.save(any(DeclarationAvancement.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        DeclarationAvancement declaration = new DeclarationAvancement(null, 40, "Test");
        DeclarationAvancement resultat = declarationService.add(7L, declaration);

        assertThat(resultat.getDateDeclaration()).isEqualTo(LocalDate.now());
        assertThat(resultat.getStage()).isSameAs(stage);
        assertThat(stage.getDeclarations()).contains(declaration);
    }

    @Test
    void refuseUnPourcentageInvalide() {
        assertThatThrownBy(() -> declarationService.add(1L,
                new DeclarationAvancement(LocalDate.now(), 120, "Test")))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("entre 0 et 100");

        verifyNoInteractions(stageRepository, declarationRepository);
    }
}
