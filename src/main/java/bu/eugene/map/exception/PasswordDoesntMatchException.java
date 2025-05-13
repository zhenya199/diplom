package bu.eugene.map.exception;

public class PasswordDoesntMatchException extends RuntimeException {
    public PasswordDoesntMatchException(String message) {
        super(message);
    }
}
