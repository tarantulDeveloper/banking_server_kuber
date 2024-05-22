package kg.saimatelecom.clientservice.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ClientAccountNotFoundException.class, ResourceNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handlerClientAccountNotFoundException(RuntimeException ex,
                                                               WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(HttpStatus.NOT_FOUND.value());
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setDescription(request.getDescription(false));
        return errorResponse;
    }

    @ExceptionHandler({IllegalArgumentException.class,
            UserIsAlreadyRegisteredException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse badRequestHandling(RuntimeException re, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        errorResponse.setMessage(re.getMessage());
        errorResponse.setDescription(request.getDescription(false));
        return errorResponse;
    }

}
