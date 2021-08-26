package commons.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * This class intercepts all kinds of errors thanks to @ExceptionHandler.
 * If we receive a BusinessResourceException, the error will contain all the informations provided by this error.
 * If we reveive any other kind of error, http status will be 500-INTERNAL_SERVER_ERROR.
 * 
 *<p>
 *The original code from <a href="https://bnguimgo.developpez.com/tutoriels/spring/services-rest-avec-springboot-et-spring-resttemplate/?page=premiere-partie-le-serveur">
 *developpez.com
 *</a>
 *</p>
 *
 */
@Slf4j
@ControllerAdvice 
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler{
	
	/**
	 * Exception handler for the BusinessResourceException.class
	 * <p>
	 * Transfer the informations from BusinessResourceException to a BusinessResourceExceptionDTO.
	 * </p>
	 * @param req the HttpServletRequest, it is used to return the request url that generated the error in the BusinessResourceExceptionDTO.
	 * @param ex the BusinessResourceException object that contains all informations of the error.
	 * @return a ResponseEntity with body=BusinessResourceExceptionDTO and HttpStatus=BusinessResourceException.status
	 */
	@ExceptionHandler(BusinessResourceException.class)
    public ResponseEntity<BusinessResourceExceptionDTO> businessResourceError(HttpServletRequest req, BusinessResourceException ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setStatus(ex.getStatus());
        response.setErrorCode(ex.getErrorCode());
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString());
        log.error("Status: {}, message: {}" ,ex.getStatus() ,ex.getMessage());
        return new ResponseEntity<>(response, ex.getStatus());
    }
    
	/**
	 * Exception handler for exceptions other than BusinessResourceException.class
	 * <p>
	 * Transfer the informations from Exception to a BusinessResourceExceptionDTO. Note that by default error code will be "Technical Error"
	 * and httpstatus will be 500-INTERNAL_SERVER_ERROR.
	 * </p>
	 * @param req the HttpServletRequest, it is used to return the request url that generated the error in the BusinessResourceExceptionDTO.
	 * @param ex the Exception object that contains informations of the error.
	 * @return a ResponseEntity with body=BusinessResourceExceptionDTO and HttpStatus=INTERNAL_SERVER_ERROR
	 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BusinessResourceExceptionDTO> unknowError(HttpServletRequest req, Exception ex) {
        BusinessResourceExceptionDTO response = new BusinessResourceExceptionDTO();
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setErrorCode("Technical Error");
        response.setErrorMessage(ex.getMessage());
        response.setRequestURL(req.getRequestURL().toString()); 
        log.error("Status: {}, message: {}" ,HttpStatus.INTERNAL_SERVER_ERROR ,ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
}