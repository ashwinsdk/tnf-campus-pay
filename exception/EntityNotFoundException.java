package exception;

public class EntityNotFoundException extends Exception { // Or extends CampusPayException if you have a base parent class
    public EntityNotFoundException(String message) {
        super(message);
    }
}
