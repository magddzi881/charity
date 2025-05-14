package pl.sii.charity.error;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(Long id) {
        super("Event with ID " + id + " not found.");
    }
}