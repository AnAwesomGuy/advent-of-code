package net.anawesomguy.adventofcode;

/**
 * Thrown when a {@link Puzzle} receives an unexpected or invalid input.
 */
public class InvalidInputException extends RuntimeException {
    public InvalidInputException() {
        super();
    }
    public InvalidInputException(String message) {
        super(message);
    }
    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }
    public InvalidInputException(Throwable cause) {
        super(cause);
    }
}
