package tn.iit.stages.controller.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.DeclarationAvancementRepository;
import tn.iit.stages.repository.LivrableRepository;
import tn.iit.stages.repository.StageRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Verifie les actions de l'acteur "etudiant stagiaire" exposees en SSR :
 * declaration d'avancement et depot de livrable.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EtudiantMvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private DeclarationAvancementRepository declarationRepository;

    @Autowired
    private LivrableRepository livrableRepository;

    private Stage premierStageAvecEtudiant() {
        return stageRepository.findAll().stream()
                .filter(s -> s.getEtudiant() != null)
                .findFirst()
                .orElseThrow();
    }

    @Test
    void declareUnAvancementDepuisLEspaceEtudiant() throws Exception {
        Stage stage = premierStageAvecEtudiant();
        Long etudiantId = stage.getEtudiant().getId();
        long nombreAvant = declarationRepository.count();

        mockMvc.perform(post("/etudiants/{id}/avancement", etudiantId)
                        .param("dateDeclaration", "2026-12-05")
                        .param("pourcentage", "65")
                        .param("commentaire", "Test MVC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/etudiants/" + etudiantId));

        assertThat(declarationRepository.count()).isEqualTo(nombreAvant + 1);
    }

    @Test
    void deposeUnLivrableDepuisLEspaceEtudiant() throws Exception {
        Stage stage = premierStageAvecEtudiant();
        Long etudiantId = stage.getEtudiant().getId();
        long nombreAvant = livrableRepository.count();

        mockMvc.perform(post("/etudiants/{id}/livrables", etudiantId)
                        .param("titre", "Livrable MVC")
                        .param("url", "https://example.com/livrable-mvc")
                        .param("description", "Test MVC"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/etudiants/" + etudiantId));

        assertThat(livrableRepository.count()).isEqualTo(nombreAvant + 1);
    }
}
