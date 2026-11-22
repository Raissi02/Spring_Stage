package tn.iit.stages.model;

/**
 * Nature d'un retour de l'enseignant encadrant.
 */
public enum TypeObservation {

    OBSERVATION("Observation"),
    RECOMMANDATION("Recommandation");

    private final String label;

    TypeObservation(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
