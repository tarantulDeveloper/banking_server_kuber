package kg.saimatelecom.clientservice.exceptions;

public class UserIsAlreadyRegisteredException extends RuntimeException {
    public UserIsAlreadyRegisteredException() {
        super("User is already exists");
    }
}