/**
 * Custom exception thrown when a library operation fails,
 * for example because of invalid input, a duplicate ID or a book that does not
 * exist.
 *
 * @author Raja Muhammad Abdussamie Tahir
 * @author Arsal Zaheer
 */
public class LibraryException extends Exception {

    /**
     * Creates a new exception with a message that explains the problem.
     *
     * @param message the error message shown to the user
     */
    public LibraryException(String message) {
        super(message);
    }
}