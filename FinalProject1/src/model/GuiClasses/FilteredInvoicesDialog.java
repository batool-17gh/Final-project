package model.GuiClasses;
import model.MangClasses.Invoice;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilteredInvoicesDialog extends JDialog {
    private List<Invoice> filteredInvoices; // List to hold filtered invoices to display
    private DefaultTableModel tableModel;   // Model for the JTable to manage data
    private JTable table;                    // Table to show invoice data

    private JLabel taxTotalLabel;            // Label to display total tax
    private JLabel grandTotalLabel;          // Label to display grand total amount

    public FilteredInvoicesDialog(JFrame parent, List<Invoice> filteredInvoices) {
        super(parent, "Filtered Invoices", true); // Modal dialog with title
        this.filteredInvoices = filteredInvoices;

        setLayout(new BorderLayout(10, 10));
        setSize(700, 400);
        setLocationRelativeTo(parent); // Center dialog relative to parent frame

        // Initialize table model with column names
        tableModel = new DefaultTableModel(new Object[]{"Invoice Number", "Merchant", "Date", "Amount", "Tax", "Total"}, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Custom cell renderer to color the "Total" column for better visibility
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            private final Color lightBlue = new Color(173, 216, 230);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(lightBlue); // Set background color to light blue
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER); // Add table inside a scroll pane in center

        // Create labels to show totals with bold font
        taxTotalLabel = new JLabel();
        grandTotalLabel = new JLabel();

        taxTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        grandTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Panel to hold total labels on left and right sides
        JPanel totalsPanel = new JPanel(new BorderLayout());
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        totalsPanel.add(taxTotalLabel, BorderLayout.WEST);
        totalsPanel.add(grandTotalLabel, BorderLayout.EAST);

        // Separator line for UI clarity
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(separator.getPreferredSize().width, 2));

        // Button to export filtered invoices to a CSV file
        JButton btnDownload = new JButton("Invoices Download");
        btnDownload.setBackground(new Color(25, 25, 112));
        btnDownload.setForeground(Color.white);
        btnDownload.addActionListener(e -> exportFilteredInvoices());

        // Button to close the dialog
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(178, 34, 34));
        btnClose.setForeground(Color.white);
        btnClose.addActionListener(e -> dispose());

        // Panel to hold the buttons side by side
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btnDownload);
        buttonsPanel.add(btnClose);

        // Bottom panel stacking totals, separator, and buttons vertically
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        bottomPanel.add(totalsPanel);
        bottomPanel.add(separator);
        bottomPanel.add(buttonsPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load the filtered invoice data into the table and update totals
        loadTableData();
    }

    // Load invoice data into the table model
    private void loadTableData() {
        tableModel.setRowCount(0); // Clear previous rows
        for (Invoice inv : filteredInvoices) {
            tableModel.addRow(new Object[]{
                    inv.getInvoiceNumber(),
                    inv.getMerchantName(),
                    inv.getDate(),
                    inv.getAmount(),
                    inv.getTax(),
                    inv.getTotalAmount()
            });
        }
        updateTotals(); // Update the total tax and grand total labels
    }

    // Calculate and update totals for tax and grand total
    private void updateTotals() {
        double taxSum = 0;
        double totalSum = 0;

        for (Invoice inv : filteredInvoices) {
            taxSum += inv.getTax();
            totalSum += inv.getTotalAmount();
        }

        // Format and display the totals in the labels
        taxTotalLabel.setText("Total Tax: " + String.format(java.util.Locale.US, "%.2f", taxSum));
        grandTotalLabel.setText("Grand Total: " + String.format(java.util.Locale.US, "%.2f", totalSum));
    }

    // Export the filtered invoices list to a CSV file
    private void exportFilteredInvoices() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return; // User canceled save dialog

        String filename = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filename.endsWith(".csv")) {
            filename += ".csv"; // Ensure file extension is .csv
        }

        try (PrintWriter pw = new PrintWriter(filename)) {
            // Write CSV header
            pw.println("Invoice No;Merchant;Date;Amount;Tax;Total");

            // Write each invoice as a CSV row
            for (Invoice inv : filteredInvoices) {
                pw.printf(java.util.Locale.US, "%d;\"%s\";\"%s\";%.2f;%.2f;%.2f%n",
                        inv.getInvoiceNumber(),
                        inv.getMerchantName(),
                        inv.getDate(),
                        inv.getAmount(),
                        inv.getTax(),
                        inv.getTotalAmount());
            }

            // Show success message
            JOptionPane.showMessageDialog(this, "Filtered invoices exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            // Show error message if export fails
            JOptionPane.showMessageDialog(this, "Error exporting invoices: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
