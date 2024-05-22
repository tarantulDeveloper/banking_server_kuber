package kg.saimatelecom.catalog.exceptions;

public class PurchaseQuantityException extends RuntimeException {
    public PurchaseQuantityException() {
        super("Not enough products");
    }
}
