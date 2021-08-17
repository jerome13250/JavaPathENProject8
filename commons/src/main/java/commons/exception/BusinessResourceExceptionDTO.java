package commons.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class BusinessResourceExceptionDTO {
 
    private String errorCode;
    private String errorMessage;
	private String requestURL;
	private HttpStatus status;

}