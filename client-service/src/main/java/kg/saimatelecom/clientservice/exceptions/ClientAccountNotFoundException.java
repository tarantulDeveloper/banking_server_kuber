package kg.saimatelecom.clientservice.exceptions;

public class ClientAccountNotFoundException extends RuntimeException {
    public ClientAccountNotFoundException(String message) {
        super(message);
    }
}