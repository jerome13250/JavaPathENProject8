package commons.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * This class is used to transfer the error message. 
 *<p>
 *The original code from <a href="https://bnguimgo.developpez.com/tutoriels/spring/services-rest-avec-springboot-et-spring-resttemplate/?page=premiere-partie-le-serveur">
 *developpez.com
 *</a>
 *</p>
 *
 */
@Data
public class BusinessResourceExceptionDTO {
 
    private String errorCode;
    private String errorMessage;
	private String requestURL;
	private HttpStatus status;

}