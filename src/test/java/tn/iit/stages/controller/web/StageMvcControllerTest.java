package tn.iit.stages.controller.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tn.iit.stages.model.Stage;
import tn.iit.stages.repository.ReunionRepository;
import tn.iit.stages.repository.StageRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Test de non-regression : le parametre d'URL {id} (stage) ne doit pas etre
 * lie a l'attribut "id" de l'entite enfant (Reunion), sinon Hibernate la
 * considere comme detachee ("detached entity passed to persist").
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StageMvcControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StageRepository stageRepository;

    @Autowired
    private ReunionRepository reunionRepository;

    @Test
    void ajouteUneReunionDepuisLeFormulaire() throws Exception {
        Stage stage = stageRepository.findAll().get(0);
        long nombreAvant = reunionRepository.count();

        mockMvc.perform(post("/stages/{id}/reunions", stage.getId())
                        .param("dateReunion", "2026-12-01")
                        .param("objet", "Reunion de test MVC")
                        .param("compteRendu", "Compte rendu de test"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/stages/" + stage.getId()));

        assertThat(reunionRepository.count()).isEqualTo(nombreAvant + 1);
    }
}
