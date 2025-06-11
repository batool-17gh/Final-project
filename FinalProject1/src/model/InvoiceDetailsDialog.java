package model;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;

public class InvoiceDetailsDialog extends JDialog {

    private static final Color DARK_BLUE = new Color(0, 0, 139);
    private static final Color DELETE_RED = new Color(178, 34, 34); // نفس لون زر الحذف بالواجهة الرئيسية

    public InvoiceDetailsDialog(JFrame parent, Invoice invoice) {
        super(parent, "Invoice Details", true);
        setLayout(new BorderLayout(10, 10));
        setSize(650, 350);
        setLocationRelativeTo(parent);

        DefaultTableModel model = new DefaultTableModel(
                new Object[]{"Invoice Number", "Merchant", "Date", "Amount", "Tax", "Total"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable table = new JTable(model);

        model.addRow(new Object[]{
                invoice.getInvoiceNumber(),
                invoice.getMerchantName(),
                invoice.getDate(),
                invoice.getAmount(),
                invoice.getTax(),
                invoice.getTotalAmount()
        });

        table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        table.setRowHeight(30);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 0, 120));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));

        DefaultTableCellRenderer totalRenderer = new DefaultTableCellRenderer() {
            private final Color lightBlue = new Color(173, 216, 230);
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(lightBlue);
                return c;
            }
        };
        table.getColumnModel().getColumn(5).setCellRenderer(totalRenderer);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JButton btnExport = new JButton("Invoice Download");
        btnExport.setBackground(DARK_BLUE);
        btnExport.setForeground(Color.white);
        btnExport.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnExport.setFocusPainted(false);

        JButton btnClose = new JButton("Close");
        btnClose.setBackground(DELETE_RED); // استخدام نفس اللون الأحمر لزر الحذف
        btnClose.setForeground(Color.WHITE);
        btnClose.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnClose.setFocusPainted(false);

        bottomPanel.add(btnExport);
        bottomPanel.add(btnClose);
        add(bottomPanel, BorderLayout.SOUTH);

        btnExport.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(this);
            if (result != JFileChooser.APPROVE_OPTION) return;

            String filename = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filename.endsWith(".csv")) {
                filename += ".csv";
            }

            try (PrintWriter pw = new PrintWriter(filename)) {
                pw.println("Invoice No;Merchant;Date;Amount;Tax;Total");
                pw.printf(java.util.Locale.US, "%d;\"%s\";\"%s\";%.2f;%.2f;%.2f%n",
                        invoice.getInvoiceNumber(),
                        invoice.getMerchantName(),
                        invoice.getDate(),
                        invoice.getAmount(),
                        invoice.getTax(),
                        invoice.getTotalAmount());
                JOptionPane.showMessageDialog(this, "Invoice exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting invoice: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClose.addActionListener(e -> dispose());
    }
}