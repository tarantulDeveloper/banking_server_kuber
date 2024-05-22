package kg.saimatelecom.catalog.exceptions;

public class DivisionByZeroException extends RuntimeException {
    public DivisionByZeroException() {
        super("Division by zero exception");
    }
}

