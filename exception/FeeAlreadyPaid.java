package exception;

public class FeeAlreadyPaid extends RuntimeException {
    public FeeAlreadyPaid(String message) {
        super(message);
    }
}
