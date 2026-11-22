package tn.iit.stages.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Gestion centralisee des erreurs de l'API REST (format RFC 7807 ProblemDetail).
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail ressourceIntrouvable(ResourceNotFoundException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        probleme.setTitle("Ressource introuvable");
        return probleme;
    }

    @ExceptionHandler(BusinessException.class)
    public ProblemDetail regleMetier(BusinessException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        probleme.setTitle("Regle metier non respectee");
        return probleme;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail validation(MethodArgumentNotValidException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, "Les donnees envoyees sont invalides.");
        probleme.setTitle("Validation echouee");
        Map<String, String> erreurs = new LinkedHashMap<>();
        for (FieldError erreur : ex.getBindingResult().getFieldErrors()) {
            erreurs.putIfAbsent(erreur.getField(), erreur.getDefaultMessage());
        }
        probleme.setProperty("erreurs", erreurs);
        return probleme;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ProblemDetail parametreManquant(MissingServletRequestParameterException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Le parametre obligatoire '" + ex.getParameterName() + "' est manquant.");
        probleme.setTitle("Parametre obligatoire manquant");
        return probleme;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail parametreInvalide(MethodArgumentTypeMismatchException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Valeur invalide pour le parametre '" + ex.getName() + "'.");
        probleme.setTitle("Parametre invalide");
        return probleme;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail contrainteIntegrite(DataIntegrityViolationException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "L'operation viole une contrainte d'integrite (email ou numero deja utilise).");
        probleme.setTitle("Conflit de donnees");
        return probleme;
    }
}
