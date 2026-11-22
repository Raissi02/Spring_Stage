package tn.iit.stages.model;

/**
 * Etats possibles d'un stage.
 */
public enum StatutStage {

    EN_ATTENTE("En attente"),
    EN_COURS("En cours"),
    TERMINE("Termine"),
    VALIDE("Valide");

    private final String label;

    StatutStage(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
