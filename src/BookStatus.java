/**
 * Represents the possible status values of a book in the library.
 * A book can be Available, Issued or Reserved.
 *
 * @author Raja Muhammad Abdussamie Tahir
 * @author Arsal Zaheer
 */
public enum BookStatus {

    /** The book is on the shelf and can be issued. */
    AVAILABLE("Available"),

    /** The book has been issued to a member. */
    ISSUED("Issued"),

    /** The book has been reserved by a member. */
    RESERVED("Reserved");

    /** The readable label shown in the GUI. */
    private final String label;

    /**
     * Creates a status with a readable label.
     *
     * @param label the text shown to the user
     */
    BookStatus(String label) {
        this.label = label;
    }

    /**
     * Returns the readable label of this status.
     *
     * @return the label, for example "Available"
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the readable label so the status displays nicely in lists and tables.
     *
     * @return the label of this status
     */
    @Override
    public String toString() {
        return label;
    }
}