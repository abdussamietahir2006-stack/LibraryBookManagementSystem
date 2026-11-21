import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Swing graphical user interface for the Library Book Management System.
 * Lets the user add, view, update and delete books and change their status.
 * All rules are checked by the {@link Library} class, and any error is shown
 * to the user in a message dialog.
 *
 * @author Raja Muhammad Abdussamie Tahir
 * @author Arsal Zaheer
 */
public class LibraryGUI extends JFrame {

    /** The library that holds all books and business logic. */
    private final Library library;

    /** Text field for the book ID. */
    private final JTextField idField = new JTextField(10);

    /** Text field for the book title. */
    private final JTextField titleField = new JTextField(22);

    /** Text field for the book author. */
    private final JTextField authorField = new JTextField(22);

    /** Drop-down list for choosing the book status. */
    private final JComboBox<BookStatus> statusBox = new JComboBox<>(BookStatus.values());

    /** Table model that holds the rows shown in the table. */
    private final DefaultTableModel tableModel;

    /** Table that displays all books. */
    private final JTable table;

    /** Label at the bottom that shows the result of the last action. */
    private final JLabel messageLabel = new JLabel(" ");

    /**
     * Creates the main window and builds all GUI components.
     *
     * @param library the library whose books are managed by this window
     */
    public LibraryGUI(Library library) {
        super("Library Book Management System");
        this.library = library;

        tableModel = new DefaultTableModel(new Object[] { "ID", "Title", "Author", "Status" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);

        buildLayout();
        refreshTable();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(850, 580);
        setLocationRelativeTo(null);
    }

    /**
     * Builds the form, the buttons, the table and the message bar.
     */
    private void buildLayout() {
        Font font = new Font("Segoe UI", Font.PLAIN, 15);

        // ----- Form panel -----
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Book Details"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 8, 6, 8);
        c.anchor = GridBagConstraints.WEST;

        addFormRow(formPanel, c, 0, "Book ID:", idField, font);
        addFormRow(formPanel, c, 1, "Title:", titleField, font);
        addFormRow(formPanel, c, 2, "Author:", authorField, font);
        statusBox.setFont(font);
        addFormRow(formPanel, c, 3, "Status:", statusBox, font);

        // ----- Button panel -----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttonPanel.add(createButton("Add Book", font, e -> addBook()));
        buttonPanel.add(createButton("View All", font, e -> viewAllBooks()));
        buttonPanel.add(createButton("Update Book", font, e -> updateBook()));
        buttonPanel.add(createButton("Delete Book", font, e -> deleteBook()));
        buttonPanel.add(createButton("Set Status", font, e -> setStatus()));
        buttonPanel.add(createButton("Clear", font, e -> clearForm()));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // ----- Table -----
        table.setFont(font);
        table.setRowHeight(26);
        table.getTableHeader().setFont(font.deriveFont(Font.BOLD));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("All Books"));

        // ----- Message bar -----
        messageLabel.setFont(font);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

        JPanel root = new JPanel(new BorderLayout(8, 8));
        root.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        root.add(topPanel, BorderLayout.NORTH);
        root.add(scrollPane, BorderLayout.CENTER);
        root.add(messageLabel, BorderLayout.SOUTH);
        setContentPane(root);
    }

    /**
     * Adds one label and one input component as a row of the form.
     *
     * @param panel the form panel
     * @param c     the layout constraints
     * @param row   the row number
     * @param text  the label text
     * @param field the input component
     * @param font  the font to use
     */
    private void addFormRow(JPanel panel, GridBagConstraints c, int row,
            String text, java.awt.Component field, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        field.setFont(font);
        c.gridx = 0;
        c.gridy = row;
        panel.add(label, c);
        c.gridx = 1;
        panel.add(field, c);
    }

    /**
     * Creates a button with a font and an action.
     *
     * @param text   the button text
     * @param font   the font to use
     * @param action the action to run when the button is clicked
     * @return the new button
     */
    private JButton createButton(String text, Font font, java.awt.event.ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.addActionListener(action);
        return button;
    }

    /**
     * Reads the form and adds a new book. Shows an error if the input is invalid.
     */
    private void addBook() {
        try {
            int id = Library.parseId(idField.getText());
            Book book = library.addBook(id, titleField.getText(), authorField.getText());
            refreshTable();
            clearForm();
            showSuccess("Book added: " + book);
        } catch (LibraryException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Shows all books in the table, or a message if the library is empty.
     */
    private void viewAllBooks() {
        refreshTable();
        if (library.isEmpty()) {
            JOptionPane.showMessageDialog(this, "The library is empty. No books to display.",
                    "View All Books", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showSuccess("Showing all " + library.size() + " book(s).");
        }
    }

    /**
     * Updates the title and author of the book whose ID is in the form.
     */
    private void updateBook() {
        try {
            int id = Library.parseId(idField.getText());
            Book book = library.updateBook(id, titleField.getText(), authorField.getText());
            refreshTable();
            showSuccess("Book updated: " + book);
        } catch (LibraryException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Deletes the book whose ID is in the form, after asking the user to confirm.
     */
    private void deleteBook() {
        try {
            int id = Library.parseId(idField.getText());
            Book book = library.findBook(id);
            if (book == null) {
                throw new LibraryException("No book found with ID " + id + ".");
            }
            int choice = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete:\n" + book + " ?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                library.deleteBook(id);
                refreshTable();
                clearForm();
                showSuccess("Book deleted: " + book);
            }
        } catch (LibraryException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Sets the status chosen in the drop-down on the book whose ID is in the form.
     */
    private void setStatus() {
        try {
            int id = Library.parseId(idField.getText());
            BookStatus status = (BookStatus) statusBox.getSelectedItem();
            Book book = library.setStatus(id, status);
            refreshTable();
            showSuccess("Status changed: " + book);
        } catch (LibraryException ex) {
            showError(ex.getMessage());
        }
    }

    /**
     * Reloads all books from the library into the table.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Book> books = library.getAllBooks();
        for (Book book : books) {
            tableModel.addRow(new Object[] {
                    book.getId(), book.getTitle(), book.getAuthor(), book.getStatus()
            });
        }
        if (books.isEmpty()) {
            messageLabel.setForeground(Color.DARK_GRAY);
            messageLabel.setText("The library is empty. Add a book to get started.");
        }
    }

    /**
     * Copies the selected table row into the form so it can be edited.
     */
    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        idField.setText(String.valueOf(tableModel.getValueAt(row, 0)));
        titleField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        authorField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        statusBox.setSelectedItem(tableModel.getValueAt(row, 3));
    }

    /**
     * Empties all form fields and clears the table selection.
     */
    private void clearForm() {
        table.clearSelection();
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        statusBox.setSelectedItem(BookStatus.AVAILABLE);
        idField.requestFocus();
    }

    /**
     * Shows a success message in green in the message bar.
     *
     * @param message the message to show
     */
    private void showSuccess(String message) {
        messageLabel.setForeground(new Color(0, 128, 0));
        messageLabel.setText(message);
    }

    /**
     * Shows an error in a dialog box and in red in the message bar.
     *
     * @param message the error message to show
     */
    private void showError(String message) {
        messageLabel.setForeground(Color.RED);
        messageLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}