package rocketseat.com.passinserver.domain.attendee.exceptions;

public class AttendeeAlreadyExistsException extends RuntimeException {
    public AttendeeAlreadyExistsException(String message){
        super(message);
    };
}
