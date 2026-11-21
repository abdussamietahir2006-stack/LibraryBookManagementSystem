import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the collection of books and contains all the business logic:
 * adding, viewing, updating, deleting and changing the status of books.
 * All input is validated here, and errors are reported with
 * {@link LibraryException}.
 *
 * Books are stored in a LinkedHashMap keyed by ID. This gives fast O(1)
 * duplicate-ID checks and lookups while keeping the order in which books were
 * added.
 *
 * @author Raja Muhammad Abdussamie Tahir
 * @author Arsal Zaheer
 */
public class Library {

    /** Stores all books, keyed by their unique ID, in insertion order. */
    private final Map<Integer, Book> books = new LinkedHashMap<>();

    /**
     * Creates an empty library.
     */
    public Library() {
        // starts with no books
    }

    /**
     * Converts the text typed by the user into a valid book ID.
     *
     * @param text the ID entered by the user
     * @return the ID as a positive integer
     * @throws LibraryException if the text is empty, not a number, or not positive
     */
    public static int parseId(String text) throws LibraryException {
        if (text == null || text.trim().isEmpty()) {
            throw new LibraryException("Book ID cannot be empty.");
        }
        int id;
        try {
            id = Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            throw new LibraryException("Book ID must be a whole number.");
        }
        if (id <= 0) {
            throw new LibraryException("Book ID must be a positive number.");
        }
        return id;
    }

    /**
     * Checks that the title and author are not empty.
     *
     * @param title  the title to check
     * @param author the author to check
     * @throws LibraryException if the title or the author is empty
     */
    private void validateDetails(String title, String author) throws LibraryException {
        if (title == null || title.trim().isEmpty()) {
            throw new LibraryException("Title cannot be empty.");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new LibraryException("Author cannot be empty.");
        }
    }

    /**
     * Adds a new book to the library with the status Available.
     *
     * @param id     the unique ID of the new book
     * @param title  the title of the new book
     * @param author the author of the new book
     * @return the book that was added
     * @throws LibraryException if the ID is not positive, already exists,
     *                          or the title or author is empty
     */
    public Book addBook(int id, String title, String author) throws LibraryException {
        if (id <= 0) {
            throw new LibraryException("Book ID must be a positive number.");
        }
        validateDetails(title, author);
        if (books.containsKey(id)) {
            throw new LibraryException("A book with ID " + id + " already exists.");
        }
        Book book = new Book(id, title.trim(), author.trim());
        books.put(id, book);
        return book;
    }

    /**
     * Returns all books in the order they were added.
     *
     * @return a list of all books (empty if the library has no books)
     */
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    /**
     * Finds a book by its ID.
     *
     * @param id the ID to search for
     * @return the matching book, or null if no book has this ID
     */
    public Book findBook(int id) {
        return books.get(id);
    }

    /**
     * Checks whether the library has no books.
     *
     * @return true if the library is empty, otherwise false
     */
    public boolean isEmpty() {
        return books.isEmpty();
    }

    /**
     * Returns the number of books in the library.
     *
     * @return the total number of books
     */
    public int size() {
        return books.size();
    }

    /**
     * Updates the title and author of an existing book. The ID cannot be changed.
     *
     * @param id     the ID of the book to update
     * @param title  the new title
     * @param author the new author
     * @return the updated book
     * @throws LibraryException if no book has this ID, or the title or author is
     *                          empty
     */
    public Book updateBook(int id, String title, String author) throws LibraryException {
        Book book = books.get(id);
        if (book == null) {
            throw new LibraryException("No book found with ID " + id + ".");
        }
        validateDetails(title, author);
        book.setTitle(title.trim());
        book.setAuthor(author.trim());
        return book;
    }

    /**
     * Deletes a book from the library.
     *
     * @param id the ID of the book to delete
     * @return the book that was removed
     * @throws LibraryException if no book has this ID
     */
    public Book deleteBook(int id) throws LibraryException {
        Book removed = books.remove(id);
        if (removed == null) {
            throw new LibraryException("No book found with ID " + id + ".");
        }
        return removed;
    }

    /**
     * Changes the status of a book to Available, Issued or Reserved.
     *
     * @param id     the ID of the book
     * @param status the new status
     * @return the book with its updated status
     * @throws LibraryException if no book has this ID or no status was chosen
     */
    public Book setStatus(int id, BookStatus status) throws LibraryException {
        Book book = books.get(id);
        if (book == null) {
            throw new LibraryException("No book found with ID " + id + ".");
        }
        if (status == null) {
            throw new LibraryException("Please choose a status.");
        }
        book.setStatus(status);
        return book;
    }
}