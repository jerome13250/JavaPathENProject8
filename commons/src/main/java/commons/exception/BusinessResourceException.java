package commons.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * Unchecked exception that is used for every kind of errors, it can be used in every sub-projects.
 *<p>
 *The original code from <a href="https://bnguimgo.developpez.com/tutoriels/spring/services-rest-avec-springboot-et-spring-resttemplate/?page=premiere-partie-le-serveur">
 *developpez.com
 *</a>
 *</p>
 *
 */
@Getter
@Setter
public class BusinessResourceException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final String errorCode;
    private final HttpStatus status;

    /**
     * Constructor for BusinessResourceException
     * @param errorCode the error code, a short string defining the error type.
     * @param message the message error, a long string describing the error with useful info for debugging.
     * @param status the http status that the API must repond when this error occurs.
     */
    public BusinessResourceException(String errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }


}