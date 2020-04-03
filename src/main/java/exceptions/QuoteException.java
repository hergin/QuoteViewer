package exceptions;

public class QuoteException extends RuntimeException {
    public QuoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
