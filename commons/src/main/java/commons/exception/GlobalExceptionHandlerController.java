package commons.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice//(basePackages = {"tourGuide"} ) 
public class GlobalExceptionHandlerController extends ResponseEntityExceptionHandler{
	
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
    
    @ExceptionHandler(Exception.class)//toutes les autres erreurs non gérées par le service sont interceptées ici
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