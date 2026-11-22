package tn.iit.stages.controller.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tn.iit.stages.exception.ResourceNotFoundException;
import tn.iit.stages.service.StageService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StageRestController.class)
class StageRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StageService stageService;

    @Test
    void retourneLaListeDesStages() throws Exception {
        when(stageService.findByStatut(null)).thenReturn(List.of());

        mockMvc.perform(get("/api/stages"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void retourne404SiLeStageEstInconnu() throws Exception {
        when(stageService.getById(99L))
                .thenThrow(ResourceNotFoundException.of("Stage", 99L));

        mockMvc.perform(get("/api/stages/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void retourne400PourUnParametreInvalide() throws Exception {
        mockMvc.perform(get("/api/stages").param("statut", "STATUT_INCONNU"))
                .andExpect(status().isBadRequest());
    }
}
