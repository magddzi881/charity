package pl.sii.charity.error;

public class BoxNotFoundException extends RuntimeException {
    public BoxNotFoundException(Long id) {
        super("Box with ID " + id + " not found.");
    }
}