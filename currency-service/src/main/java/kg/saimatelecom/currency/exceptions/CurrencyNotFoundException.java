package kg.saimatelecom.currency.exceptions;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException() {
        super("Currency not found");
    }
}
