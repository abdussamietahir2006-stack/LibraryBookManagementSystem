import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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

    /** Dark colour used for the top header and the table header. */
    private static final Color HEADER_COLOR = new Color(30, 41, 59);
    /** Light background colour of the window. */
    private static final Color BACKGROUND = new Color(241, 245, 249);
    /** Background colour of the white cards. */
    private static final Color CARD = Color.WHITE;
    /** Colour of every second table row. */
    private static final Color STRIPE = new Color(248, 250, 252);
    /** Colour of borders and lines. */
    private static final Color BORDER = new Color(203, 213, 225);
    /** Main text colour. */
    private static final Color TEXT = new Color(15, 23, 42);
    /** Grey colour for captions and labels. */
    private static final Color MUTED = new Color(100, 116, 139);
    /** Blue accent colour. */
    private static final Color BLUE = new Color(37, 99, 235);
    /** Green colour for Available and success messages. */
    private static final Color GREEN = new Color(22, 163, 74);
    /** Amber colour for Issued and the Update button. */
    private static final Color AMBER = new Color(217, 119, 6);
    /** Purple colour for Reserved and the Set Status button. */
    private static final Color PURPLE = new Color(124, 58, 237);
    /** Red colour for Delete and error messages. */
    private static final Color RED = new Color(220, 38, 38);
    /** Slate colour for the Clear button. */
    private static final Color SLATE = new Color(71, 85, 105);
    /** Normal font used across the window. */
    private static final Font FONT = new Font("Segoe UI", Font.PLAIN, 15);
    /** Bold version of the normal font. */
    private static final Font BOLD = FONT.deriveFont(Font.BOLD);

    /** The library that holds all books and business logic. */
    private final Library library;

    /** Text field for the book ID. */
    private final JTextField idField = new JTextField(18);
    /** Text field for the book title. */
    private final JTextField titleField = new JTextField(18);
    /** Text field for the book author. */
    private final JTextField authorField = new JTextField(18);
    /** Drop-down list for choosing the book status. */
    private final JComboBox<BookStatus> statusBox = new JComboBox<>(BookStatus.values());

    /** Table model that holds the rows shown in the table. */
    private final DefaultTableModel tableModel;
    /** Table that displays all books. */
    private final JTable table;

    /** Shows the total number of books. */
    private final JLabel totalLabel = new JLabel("0");
    /** Shows the number of available books. */
    private final JLabel availableLabel = new JLabel("0");
    /** Shows the number of issued books. */
    private final JLabel issuedLabel = new JLabel("0");
    /** Shows the number of reserved books. */
    private final JLabel reservedLabel = new JLabel("0");

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
        setMinimumSize(new Dimension(980, 620));
        setSize(1080, 700);
        setLocationRelativeTo(null);
    }

    // ===================== Layout =====================

    /**
     * Puts the header, form card, statistics, table and message bar together.
     */
    private void buildLayout() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BACKGROUND);
        root.add(createHeader(), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(16, 16));
        content.setOpaque(false);
        content.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        content.add(createFormCard(), BorderLayout.WEST);

        JPanel right = new JPanel(new BorderLayout(0, 16));
        right.setOpaque(false);
        right.add(createStatsPanel(), BorderLayout.NORTH);
        right.add(createTableCard(), BorderLayout.CENTER);
        content.add(right, BorderLayout.CENTER);

        root.add(content, BorderLayout.CENTER);
        root.add(createMessageBar(), BorderLayout.SOUTH);
        setContentPane(root);
    }

    /**
     * Creates the dark title bar at the top of the window.
     *
     * @return the header panel
     */
    private JPanel createHeader() {
        JPanel header = new JPanel(new GridLayout(2, 1));
        header.setBackground(HEADER_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("Library Book Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Add, view, update, delete and track the status of every book");
        subtitle.setFont(FONT.deriveFont(14f));
        subtitle.setForeground(new Color(148, 163, 184));

        header.add(title);
        header.add(subtitle);
        return header;
    }

    /**
     * Creates a white card with a rounded border and a title.
     *
     * @param title the title shown at the top of the card
     * @return the card panel
     */
    private JPanel createCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(16, 18, 16, 18)));
        JLabel label = new JLabel(title);
        label.setFont(BOLD.deriveFont(17f));
        label.setForeground(TEXT);
        card.add(label, BorderLayout.NORTH);
        return card;
    }

    /**
     * Creates the card that holds the input fields and the action buttons.
     *
     * @return the form card
     */
    private JPanel createFormCard() {
        JPanel card = createCard("Book Details");
        card.setPreferredSize(new Dimension(320, 10));

        JPanel body = new JPanel(new GridBagLayout());
        body.setOpaque(false);
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;

        styleField(idField);
        styleField(titleField);
        styleField(authorField);
        statusBox.setFont(FONT);
        statusBox.setBackground(Color.WHITE);

        addField(body, c, "BOOK ID", idField);
        addField(body, c, "TITLE", titleField);
        addField(body, c, "AUTHOR", authorField);
        addField(body, c, "STATUS", statusBox);

        JPanel buttons = new JPanel(new GridLayout(3, 2, 10, 10));
        buttons.setOpaque(false);
        buttons.add(createButton("Add Book", GREEN, e -> addBook()));
        buttons.add(createButton("View All", BLUE, e -> viewAllBooks()));
        buttons.add(createButton("Update", AMBER, e -> updateBook()));
        buttons.add(createButton("Delete", RED, e -> deleteBook()));
        buttons.add(createButton("Set Status", PURPLE, e -> setStatus()));
        buttons.add(createButton("Clear", SLATE, e -> clearForm()));
        c.insets = new Insets(22, 0, 0, 0);
        body.add(buttons, c);
        c.gridy++;

        JLabel hint = new JLabel("<html>Tip: click a row in the table to load it into the form.</html>");
        hint.setFont(FONT.deriveFont(13f));
        hint.setForeground(MUTED);
        c.insets = new Insets(14, 0, 0, 0);
        body.add(hint, c);
        c.gridy++;

        c.weighty = 1;
        body.add(Box.createGlue(), c);

        card.add(body, BorderLayout.CENTER);
        return card;
    }

    /**
     * Adds a small caption and an input component to the form.
     *
     * @param body  the form panel
     * @param c     the layout constraints
     * @param text  the caption text
     * @param field the input component
     */
    private void addField(JPanel body, GridBagConstraints c, String text, JComponent field) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD.deriveFont(12f));
        label.setForeground(MUTED);
        c.insets = new Insets(10, 0, 4, 0);
        body.add(label, c);
        c.gridy++;
        c.insets = new Insets(0, 0, 0, 0);
        body.add(field, c);
        c.gridy++;
    }

    /**
     * Gives a text field padding and a border that turns blue when focused.
     *
     * @param field the text field to style
     */
    private void styleField(JTextField field) {
        field.setFont(FONT);
        Border normal = BorderFactory.createCompoundBorder(
                new LineBorder(BORDER, 1, true), BorderFactory.createEmptyBorder(7, 10, 7, 10));
        Border focused = BorderFactory.createCompoundBorder(
                new LineBorder(BLUE, 2, true), BorderFactory.createEmptyBorder(6, 9, 6, 9));
        field.setBorder(normal);
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                field.setBorder(focused);
            }

            @Override
            public void focusLost(FocusEvent e) {
                field.setBorder(normal);
            }
        });
    }

    /**
     * Creates a flat coloured button that gets darker when the mouse is over it.
     *
     * @param text   the button text
     * @param color  the background colour
     * @param action the action to run when the button is clicked
     * @return the new button
     */
    private JButton createButton(String text, Color color, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(BOLD.deriveFont(14f));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        Color hover = color.darker();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        button.addActionListener(action);
        return button;
    }

    /**
     * Creates the row of four statistic cards.
     *
     * @return the statistics panel
     */
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 12, 0));
        panel.setOpaque(false);
        panel.add(createStatCard("Total Books", totalLabel, BLUE));
        panel.add(createStatCard("Available", availableLabel, GREEN));
        panel.add(createStatCard("Issued", issuedLabel, AMBER));
        panel.add(createStatCard("Reserved", reservedLabel, PURPLE));
        return panel;
    }

    /**
     * Creates one statistic card with a coloured strip on the left.
     *
     * @param caption    the caption under the number
     * @param valueLabel the label that shows the number
     * @param color      the accent colour of the card
     * @return the statistic card
     */
    private JPanel createStatCard(String caption, JLabel valueLabel, Color color) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBackground(CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 6, 0, 0, color),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(1, 0, 1, 1, BORDER),
                        BorderFactory.createEmptyBorder(10, 14, 10, 14))));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);
        JLabel captionLabel = new JLabel(caption);
        captionLabel.setFont(FONT.deriveFont(13f));
        captionLabel.setForeground(MUTED);
        card.add(valueLabel);
        card.add(captionLabel);
        return card;
    }

    /**
     * Creates the card that holds the styled book table.
     *
     * @return the table card
     */
    private JPanel createTableCard() {
        JPanel card = createCard("All Books");

        table.setFont(FONT);
        table.setRowHeight(34);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(226, 232, 240));
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setSelectionBackground(new Color(219, 234, 254));
        table.setSelectionForeground(TEXT);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class, new BookCellRenderer());

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new HeaderRenderer());
        header.setPreferredSize(new Dimension(0, 40));
        header.setReorderingAllowed(false);

        table.getColumnModel().getColumn(0).setPreferredWidth(70);
        table.getColumnModel().getColumn(1).setPreferredWidth(280);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(BORDER, 1));
        scrollPane.getViewport().setBackground(CARD);
        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    /**
     * Creates the white bar at the bottom that shows messages.
     *
     * @return the message bar
     */
    private JPanel createMessageBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(CARD);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER),
                BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        messageLabel.setFont(BOLD.deriveFont(14f));
        bar.add(messageLabel, BorderLayout.WEST);

        JLabel credit = new JLabel("Software Construction OEL");
        credit.setFont(FONT.deriveFont(13f));
        credit.setForeground(MUTED);
        bar.add(credit, BorderLayout.EAST);
        return bar;
    }

    // ===================== Actions =====================

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

    // ===================== Helpers =====================

    /**
     * Reloads all books from the library into the table and updates the statistics.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        List<Book> books = library.getAllBooks();
        for (Book book : books) {
            tableModel.addRow(new Object[] {
                    book.getId(), book.getTitle(), book.getAuthor(), book.getStatus()
            });
        }
        updateStats(books);
        if (books.isEmpty()) {
            messageLabel.setForeground(MUTED);
            messageLabel.setText("The library is empty. Add a book to get started.");
        }
    }

    /**
     * Counts the books by status and shows the numbers in the statistic cards.
     *
     * @param books the current list of books
     */
    private void updateStats(List<Book> books) {
        int available = 0;
        int issued = 0;
        int reserved = 0;
        for (Book book : books) {
            switch (book.getStatus()) {
                case ISSUED:
                    issued++;
                    break;
                case RESERVED:
                    reserved++;
                    break;
                default:
                    available++;
            }
        }
        totalLabel.setText(String.valueOf(books.size()));
        availableLabel.setText(String.valueOf(available));
        issuedLabel.setText(String.valueOf(issued));
        reservedLabel.setText(String.valueOf(reserved));
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
        messageLabel.setForeground(GREEN);
        messageLabel.setText(message);
    }

    /**
     * Shows an error in a dialog box and in red in the message bar.
     *
     * @param message the error message to show
     */
    private void showError(String message) {
        messageLabel.setForeground(RED);
        messageLabel.setText(message);
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Returns the colour used to show a status in the table.
     *
     * @param status the book status
     * @return green for Available, amber for Issued, purple for Reserved
     */
    private static Color statusColor(BookStatus status) {
        switch (status) {
            case ISSUED:
                return AMBER;
            case RESERVED:
                return PURPLE;
            default:
                return GREEN;
        }
    }

    /**
     * Draws the table header cells with a dark background and white bold text.
     */
    private static class HeaderRenderer extends DefaultTableCellRenderer {

        /**
         * Returns the styled header cell.
         *
         * @param table      the table
         * @param value      the header text
         * @param isSelected whether the cell is selected
         * @param hasFocus   whether the cell has focus
         * @param row        the row index
         * @param column     the column index
         * @return the component used to draw the header cell
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            setBackground(HEADER_COLOR);
            setForeground(Color.WHITE);
            setFont(BOLD);
            setHorizontalAlignment(LEFT);
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            return this;
        }
    }

    /**
     * Draws table rows with stripes and shows the status in its own colour.
     */
    private static class BookCellRenderer extends DefaultTableCellRenderer {

        /**
         * Returns the styled cell for a row of the book table.
         *
         * @param table      the table
         * @param value      the cell value
         * @param isSelected whether the row is selected
         * @param hasFocus   whether the cell has focus
         * @param row        the row index
         * @param column     the column index
         * @return the component used to draw the cell
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, false, row, column);
            setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
            setFont(FONT);
            setForeground(TEXT);
            setHorizontalAlignment(LEFT);
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(row % 2 == 0 ? CARD : STRIPE);
            }
            if (value instanceof BookStatus) {
                setFont(BOLD);
                setForeground(statusColor((BookStatus) value));
                setText("\u25CF  " + value);
            }
            return this;
        }
    }
}