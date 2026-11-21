import javax.swing.SwingUtilities;

/**
 * Entry point of the Library Book Management System.
 * Starts the Swing GUI on the event dispatch thread.
 *
 * @author Raja Muhammad Abdussamie Tahir
 * @author Arsal Zaheer
 */
public class App {

    /**
     * Private constructor, because this class is only used to start the program.
     */
    private App() {
    }

    /**
     * Starts the application by creating an empty library and showing the main
     * window.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI(new Library()).setVisible(true));
    }
}