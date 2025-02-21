package bu.eugene.map.exception;

public class UsernameIsTakenException extends RuntimeException {
    public UsernameIsTakenException(String message) {
        super(message);
    }
}
