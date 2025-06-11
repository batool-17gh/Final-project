package model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FilteredInvoicesDialog extends JDialog {
    private List<Invoice> filteredInvoices;
    private DefaultTableModel tableModel;
    private JTable table;

    private JLabel taxTotalLabel;
    private JLabel grandTotalLabel;

    public FilteredInvoicesDialog(JFrame parent, List<Invoice> filteredInvoices) {
        super(parent, "Filtered Invoices", true);
        this.filteredInvoices = filteredInvoices;

        setLayout(new BorderLayout(10, 10));
        setSize(700, 400);
        setLocationRelativeTo(parent);

        // Table for invoices
        tableModel = new DefaultTableModel(new Object[]{"Invoice Number", "Merchant", "Date", "Amount", "Tax", "Total"}, 0);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

        // Color Total column
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            private final Color lightBlue = new Color(173, 216, 230);

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(lightBlue);
                return c;
            }
        });

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Create labels for totals
        taxTotalLabel = new JLabel();
        grandTotalLabel = new JLabel();

        taxTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        grandTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Panel for totals with BorderLayout to position left and right
        JPanel totalsPanel = new JPanel(new BorderLayout());
        totalsPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        totalsPanel.add(taxTotalLabel, BorderLayout.WEST);
        totalsPanel.add(grandTotalLabel, BorderLayout.EAST);

        // Small separator line
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(separator.getPreferredSize().width, 2));

        // Download button
        JButton btnDownload = new JButton("Invoices Download");
        btnDownload.setBackground(new Color(25, 25, 112));
        btnDownload.setForeground(Color.white);
        btnDownload.addActionListener(e -> exportFilteredInvoices());

        // Close button
        JButton btnClose = new JButton("Close");
        btnClose.setBackground(new Color(178, 34, 34));
        btnClose.setForeground(Color.white);
        btnClose.addActionListener(e -> dispose());

        // Panel for buttons
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

        // Load data and totals
        loadTableData();
    }

    private void loadTableData() {
        tableModel.setRowCount(0);
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
        updateTotals();
    }

    private void updateTotals() {
        double taxSum = 0;
        double totalSum = 0;

        for (Invoice inv : filteredInvoices) {
            taxSum += inv.getTax();
            totalSum += inv.getTotalAmount();
        }

        taxTotalLabel.setText("Total Tax: " + String.format(java.util.Locale.US, "%.2f", taxSum));
        grandTotalLabel.setText("Grand Total: " + String.format(java.util.Locale.US, "%.2f", totalSum));
    }

    private void exportFilteredInvoices() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        String filename = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.println("Invoice No;Merchant;Date;Amount;Tax;Total");

            for (Invoice inv : filteredInvoices) {
                pw.printf(java.util.Locale.US, "%d;\"%s\";\"%s\";%.2f;%.2f;%.2f%n",
                        inv.getInvoiceNumber(),
                        inv.getMerchantName(),
                        inv.getDate(),
                        inv.getAmount(),
                        inv.getTax(),
                        inv.getTotalAmount());
            }

            JOptionPane.showMessageDialog(this, "Filtered invoices exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting invoices: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
