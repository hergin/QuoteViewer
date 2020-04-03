package exceptions;

public class QuoteIdExistsException extends RuntimeException {
    public QuoteIdExistsException(int id) {
        super("This id [" + id + "] already exists!");
    }
}
