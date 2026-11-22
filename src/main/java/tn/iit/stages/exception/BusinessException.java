package tn.iit.stages.exception;

/**
 * Regle metier non respectee (traduite en HTTP 409 par l'API REST).
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
