package kg.saimatelecom.transactionservice.exceptions;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException() {
        super("Insufficient funds to complete this transaction");
    }
}
