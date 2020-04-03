package exceptions;

public class QuoteIdNotExistsException extends RuntimeException {
    public QuoteIdNotExistsException(int id) {
        super("This id [" + id + "] doesn't exist in the database!");
    }
}
