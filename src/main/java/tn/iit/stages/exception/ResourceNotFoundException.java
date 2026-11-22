package tn.iit.stages.exception;

/**
 * Ressource demandee inexistante (traduite en HTTP 404 par l'API REST).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException of(String ressource, Long id) {
        return new ResourceNotFoundException(ressource + " introuvable pour l'identifiant " + id);
    }
}
