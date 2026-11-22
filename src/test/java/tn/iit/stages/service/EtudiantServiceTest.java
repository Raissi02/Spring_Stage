package tn.iit.stages.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.iit.stages.exception.BusinessException;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.model.Etudiant;
import tn.iit.stages.repository.EnseignantRepository;
import tn.iit.stages.repository.EtudiantRepository;
import tn.iit.stages.repository.StageRepository;
import tn.iit.stages.service.impl.EtudiantServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @Mock
    private EnseignantRepository enseignantRepository;

    @Mock
    private StageRepository stageRepository;

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Test
    void affecteUnEnseignantEncadrant() {
        Etudiant etudiant = new Etudiant("Ben Salah", "Ahmed", "ahmed@iit.tn",
                "+216 55 000 000", "ETU-1", "GL", "3eme annee");
        etudiant.setId(1L);

        Enseignant enseignant = new Enseignant("Zayani", "Mohamed", "m.zayani@iit.tn",
                "+216 98 000 000", "Maitre de conferences", "Genie logiciel");
        enseignant.setId(2L);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(enseignantRepository.findById(2L)).thenReturn(Optional.of(enseignant));
        when(etudiantRepository.save(etudiant)).thenReturn(etudiant);

        Etudiant resultat = etudiantService.affecterEncadrant(1L, 2L);

        assertThat(resultat.getEncadrant()).isSameAs(enseignant);
        verify(etudiantRepository).save(etudiant);
    }

    @Test
    void refuseLaSuppressionSiUnStageExiste() {
        Etudiant etudiant = new Etudiant("Ben Salah", "Ahmed", "ahmed@iit.tn",
                null, "ETU-1", "GL", "3eme annee");
        etudiant.setId(1L);

        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(etudiant));
        when(stageRepository.existsByEtudiantId(1L)).thenReturn(true);

        assertThatThrownBy(() -> etudiantService.delete(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("stage");

        verify(etudiantRepository, never()).delete(any());
    }
}
