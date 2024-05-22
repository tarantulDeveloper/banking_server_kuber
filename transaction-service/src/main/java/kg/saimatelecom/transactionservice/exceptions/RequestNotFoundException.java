package kg.saimatelecom.transactionservice.exceptions;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException() {
        super("Request not found");
    }
}