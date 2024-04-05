package rocketseat.com.passinserver.domain.attendee.exceptions;

public class EventFullException extends RuntimeException {
    public EventFullException(String message) {
        super(message);
    }
}
