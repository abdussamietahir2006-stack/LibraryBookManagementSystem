/**
 * Represents a single book in the library.
 * Each book has a unique ID, a title, an author and a status.
 *
 * @author Abdussamie Tahir
 * @author Arsal Zaheer
 */
public class Book {

    /** Unique identification number of the book. */
    private final int id;

    /** Title of the book. */
    private String title;

    /** Author of the book. */
    private String author;

    /** Current status of the book (Available, Issued or Reserved). */
    private BookStatus status;

    /**
     * Creates a new book with the status set to Available.
     *
     * @param id     the unique ID of the book
     * @param title  the title of the book
     * @param author the author of the book
     */
    public Book(int id, String title, String author) {
        this(id, title, author, BookStatus.AVAILABLE);
    }

    /**
     * Creates a new book with a given status.
     *
     * @param id     the unique ID of the book
     * @param title  the title of the book
     * @param author the author of the book
     * @param status the starting status of the book
     */
    public Book(int id, String title, String author, BookStatus status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = status;
    }

    /**
     * Returns the ID of the book.
     *
     * @return the book ID
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the title of the book.
     *
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of the book.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the author of the book.
     *
     * @return the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Changes the author of the book.
     *
     * @param author the new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the current status of the book.
     *
     * @return the book status
     */
    public BookStatus getStatus() {
        return status;
    }

    /**
     * Changes the status of the book.
     *
     * @param status the new status
     */
    public void setStatus(BookStatus status) {
        this.status = status;
    }

    /**
     * Returns a short text description of the book.
     *
     * @return the book as text, for example "1 - Clean Code by Robert Martin
     *         (Available)"
     */
    @Override
    public String toString() {
        return id + " - " + title + " by " + author + " (" + status + ")";
    }
}