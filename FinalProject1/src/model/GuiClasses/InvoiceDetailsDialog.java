package model.GuiClasses;
import model.MangClasses.Invoice;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

public class InvoiceDetailsDialog extends JDialog {
    // Define some colors for consistent UI styling
    private static final Color DARK_BLUE = new Color(0, 0, 139);
    private static final Color DELETE_RED = new Color(178, 34, 34); // Same as delete button color in main UI

    // Constructor to create the dialog showing details of a single invoice
    public InvoiceDetailsDialog(JFrame parent, Invoice invoice) {
        super(parent, "Invoice Details", true);  // Set dialog title and make it modal (blocks parent)
        setLayout(new BorderLayout(10, 10));     // Set layout with padding
        setSize(650, 350);                        // Set fixed size
        setLocationRelativeTo(parent);           // Center dialog relative to parent window

        // Create a table model with column headers; cells are not editable
        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Invoice Number", "Merchant", "Date", "Amount", "Tax", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Prevent editing of table cells
            }
        };

        // Create a JTable with the model
        JTable table = new JTable(model);

        // Add one row with invoice details
        model.addRow(new Object[]{
                invoice.getInvoiceNumber(),
                invoice.getMerchantName(),
                invoice.getDate(),
                invoice.getAmount(),
                invoice.getTax(),
                invoice.getTotalAmount()
        });

        // Set font and row height for better readability
        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);

        // Customize the table header's background, foreground, and font
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 0, 120));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));

        // Create a custom cell renderer to color the "Total" column cells light blue
        DefaultTableCellRenderer totalRenderer = new DefaultTableCellRenderer() {
            private final Color lightBlue = new Color(173, 216, 230);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(lightBlue);  // Set background color for total cells
                return c;
            }
        };
        // Apply this renderer only to the "Total" column (index 5)
        table.getColumnModel().getColumn(5).setCellRenderer(totalRenderer);

        // Add the table inside a scroll pane to the center of the dialog
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Create a bottom panel for buttons with flow layout (centered)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15)); // Padding

        // Create "Export" button to save invoice details to CSV file
        JButton btnExport = new JButton("Invoice Download");
        btnExport.setBackground(DARK_BLUE);
        btnExport.setForeground(Color.white);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExport.setFocusPainted(false);  // Remove focus border

        // Create "Close" button to close the dialog
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(DELETE_RED); // Use red color consistent with delete buttons
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.setFocusPainted(false);

        // Add buttons to bottom panel
        bottomPanel.add(btnExport);
        bottomPanel.add(btnClose);

        // Add bottom panel to dialog's south area
        add(bottomPanel, BorderLayout.SOUTH);

        // Add action listener to Export button to save invoice to CSV file
        btnExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();  // Open file chooser dialog
            int result = fileChooser.showSaveDialog(this);  // Show "Save As" dialog
            if (result != JFileChooser.APPROVE_OPTION) return;  // If user cancels, exit

            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            // Append .csv extension if not present
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }

            // Write invoice data to the selected file
            try (PrintWriter pw = new PrintWriter(filename)) {
                pw.println("Invoice No;Merchant;Date;Amount;Tax;Total");  // Write CSV header
                pw.printf(java.util.Locale.US, "%d;\"%s\";\"%s\";%.2f;%.2f;%.2f%n",
                        invoice.getInvoiceNumber(),
                        invoice.getMerchantName(),
                        invoice.getDate(),
                        invoice.getAmount(),
                        invoice.getTax(),
                        invoice.getTotalAmount());
                // Show success message after export
                JOptionPane.showMessageDialog(this, "Invoice exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                // Show error message if export fails
                JOptionPane.showMessageDialog(this, "Error exporting invoice: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Add action listener to Close button to close the dialog window
        btnClose.addActionListener(e -> dispose());
    }
}
