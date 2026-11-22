package tn.iit.stages.model;

/**
 * Etats possibles d'une etape de progression du stage.
 */
public enum StatutEtape {

    A_FAIRE("A faire"),
    EN_COURS("En cours"),
    VALIDEE("Validee");

    private final String label;

    StatutEtape(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
