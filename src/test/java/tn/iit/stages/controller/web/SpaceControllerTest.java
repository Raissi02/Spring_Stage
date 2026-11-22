package tn.iit.stages.controller.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tn.iit.stages.model.Enseignant;
import tn.iit.stages.repository.EnseignantRepository;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Les trois espaces acteurs (administrateur, enseignant, etudiant) doivent
 * etre accessibles et rendus correctement.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SpaceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EnseignantRepository enseignantRepository;

    @Test
    void lesTroisEspacesSontAccessibles() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Espace administration")));

        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Espace administrateur")));

        mockMvc.perform(get("/enseignant"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Espace enseignant")));

        mockMvc.perform(get("/etudiant"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Espace etudiant")));

        Enseignant enseignant = enseignantRepository.findAll().stream()
                .findFirst()
                .orElseThrow();

        mockMvc.perform(get("/enseignant/{id}", enseignant.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Etudiants affectes")));
    }
}
