package model.GuiClasses;
import model.MangClasses.Invoice;
import model.MangClasses.InvoiceManager;
import model.MangClasses.InvoiceProcessor;
import model.MangClasses.InvoiceFilter;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class InvoiceGUI extends JFrame {
    private InvoiceManager manager = new InvoiceManager(10000);
    private InvoiceProcessor processor = new InvoiceProcessor();

    private JTextField tfInvoiceNumber = new JTextField(10);
    private JTextField tfMerchantName = new JTextField(10);
    private JTextField tfDate = new JTextField(10);
    private JTextField tfAmount = new JTextField(10);
    private JTextField tfTax = new JTextField(10);

    private JTextField tfSearchInvoiceNumber = new JTextField(10);

    private JTextField tfMerchantOrMonth = new JTextField("Amazon,05,2025", 10);

    {
        tfMerchantOrMonth.setForeground(Color.GRAY);
        tfMerchantOrMonth.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (tfMerchantOrMonth.getText().equals("Amazon,05,2025")) {
                    tfMerchantOrMonth.setText("");
                    tfMerchantOrMonth.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (tfMerchantOrMonth.getText().trim().isEmpty()) {
                    tfMerchantOrMonth.setText("Amazon,05,2025");
                    tfMerchantOrMonth.setForeground(Color.GRAY);
                }
            }
        });


    }

    private JTextArea taErrors = new JTextArea(8, 30);
    private DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"Invoice Number", "Merchant", "Date", "Amount", "Tax", "Total"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private JTable table = new JTable(tableModel);

    private JButton btnExport;

    public InvoiceGUI() {
        super("Invoice Management System");
        setLayout(new BorderLayout(10, 10));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panelAdd = new JPanel(new GridBagLayout());
        panelAdd.setBorder(BorderFactory.createTitledBorder("Add New Invoice"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);

        c.gridx = 0;
        c.gridy = 0;
        panelAdd.add(new JLabel("Invoice Number:"), c);
        c.gridx = 1;
        panelAdd.add(tfInvoiceNumber, c);
        c.gridx = 0;
        c.gridy = 1;
        panelAdd.add(new JLabel("Merchant Name:"), c);
        c.gridx = 1;
        panelAdd.add(tfMerchantName, c);
        c.gridx = 0;
        c.gridy = 2;
        panelAdd.add(new JLabel("Date (YYYY-MM-DD):"), c);
        c.gridx = 1;
        panelAdd.add(tfDate, c);
        c.gridx = 0;
        c.gridy = 3;
        panelAdd.add(new JLabel("Amount:"), c);
        c.gridx = 1;
        panelAdd.add(tfAmount, c);
        c.gridx = 0;
        c.gridy = 4;
        panelAdd.add(new JLabel("Tax:"), c);
        c.gridx = 1;
        panelAdd.add(tfTax, c);

        JButton btnAdd = new JButton("Add Invoice");
        btnAdd.setBackground(new Color(30, 144, 255));
        btnAdd.setForeground(Color.white);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        panelAdd.add(btnAdd, c);

        btnAdd.addActionListener(e -> addInvoice());

        JPanel panelSearch = new JPanel(new GridBagLayout());
        panelSearch.setBorder(BorderFactory.createTitledBorder("Search & Calculations"));
        c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);

        c.gridx = 0;
        c.gridy = 0;
        panelSearch.add(new JLabel("Search by Invoice Number:"), c);
        c.gridx = 1;
        panelSearch.add(tfSearchInvoiceNumber, c);
        JButton btnSearch = new JButton("Search");
        btnSearch.setBackground(new Color(65, 105, 225));
        btnSearch.setForeground(Color.white);
        c.gridx = 2;
        panelSearch.add(btnSearch, c);
        btnSearch.addActionListener(e -> searchInvoice());

        JLabel lblMerchantMonth = new JLabel("Merchant Name, Month (MM), and/or Year (YYYY):");
        lblMerchantMonth.setToolTipText("Example: Amazon,01,2022 OR Amazon,01 OR 2022 OR just Amazon");

        c.gridx = 0;
        c.gridy = 1;
        panelSearch.add(lblMerchantMonth, c);
        c.gridx = 1;
        panelSearch.add(tfMerchantOrMonth, c);
        JButton btnMerchantMonthSearch = new JButton("Calculate Total");
        btnMerchantMonthSearch.setBackground(new Color(100, 149, 237));
        btnMerchantMonthSearch.setForeground(Color.white);
        c.gridx = 2;
        panelSearch.add(btnMerchantMonthSearch, c);
        btnMerchantMonthSearch.addActionListener(e -> calculateByMerchantAndOrMonth());

        JPanel panelErrors = new JPanel(new BorderLayout());
        panelErrors.setBorder(BorderFactory.createTitledBorder("Error Log"));
        taErrors.setEditable(false);
        panelErrors.add(new JScrollPane(taErrors), BorderLayout.CENTER);

        JPanel panelTable = new JPanel(new BorderLayout());
        panelTable.setBorder(BorderFactory.createTitledBorder("Invoice List"));
        table.setFillsViewportHeight(true);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

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
        panelTable.add(new JScrollPane(table), BorderLayout.CENTER);

        JButton btnLoadFile = new JButton("Load From File");
        btnLoadFile.setBackground(new Color(25, 25, 112));
        btnLoadFile.setForeground(Color.white);
        btnLoadFile.addActionListener(e -> loadFromFile());

        JButton btnDelete = new JButton("Delete Selected Invoice");
        btnDelete.setBackground(new Color(178, 34, 34));
        btnDelete.setForeground(Color.white);
        btnDelete.addActionListener(e -> deleteSelectedInvoices());

        btnExport = new JButton("Export Invoices");
        btnExport.setBackground(new Color(46, 139, 87));
        btnExport.setForeground(Color.white);
        btnExport.setVisible(false);
        btnExport.addActionListener(e -> exportInvoices());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(panelAdd, BorderLayout.WEST);
        topPanel.add(panelSearch, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(btnLoadFile);
        buttonsPanel.add(btnDelete);
        buttonsPanel.add(btnExport);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(panelTable, BorderLayout.CENTER);

        JPanel bottomSouthPanel = new JPanel(new BorderLayout());
        bottomSouthPanel.add(panelErrors, BorderLayout.CENTER);
        bottomSouthPanel.add(buttonsPanel, BorderLayout.NORTH);
        bottomPanel.add(bottomSouthPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.CENTER);

        setSize(900, 700);
        setLocationRelativeTo(null);
        setVisible(true);
        getContentPane().setBackground(new Color(224, 238, 255));
    }

    private void addInvoice() {
        try {
            int number = Integer.parseInt(tfInvoiceNumber.getText().trim());
            String merchant = tfMerchantName.getText().trim();
            String date = tfDate.getText().trim();
            double amount = Double.parseDouble(tfAmount.getText().trim());
            double tax = Double.parseDouble(tfTax.getText().trim());

            if (merchant.isEmpty() || !merchant.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Merchant name must contain letters only and cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (tax < 0) {
                JOptionPane.showMessageDialog(this, "Tax must be zero or a positive number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (manager.getInvoiceByNumber(number) != null) {
                JOptionPane.showMessageDialog(this, "Invoice number already exists.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }



    Invoice invoice = new Invoice(number, merchant, date, amount, tax);
            manager.addInvoice(invoice);
            tableModel.addRow(new Object[]{number, merchant, date, amount, tax, invoice.getTotalAmount()});
            clearAddFields();

            if (!btnExport.isVisible()) btnExport.setVisible(true);

            JOptionPane.showMessageDialog(this, "Invoice added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearAddFields() {
        tfInvoiceNumber.setText("");
        tfMerchantName.setText("");
        tfDate.setText("");
        tfAmount.setText("");
        tfTax.setText("");
    }


    private void searchInvoice() {
        try {
            int number = Integer.parseInt(tfSearchInvoiceNumber.getText().trim());
            Invoice inv = manager.getInvoiceByNumber(number);
            if (inv != null) {
                InvoiceDetailsDialog dialog = new InvoiceDetailsDialog(this, inv);
                dialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Invoice not found.", "Not Found", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid invoice number.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateByMerchantAndOrMonth() {
        String input = tfMerchantOrMonth.getText().trim();
        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter merchant name, month, year, or any combination.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String merchant = null;
        String month = null;
        String year = null;

        String[] parts = input.split(",");
        for (String part : parts) {
            part = part.trim();
            if (part.matches("\\d{2}")) {
                month = part;
            } else if (part.matches("\\d{4}")) {
                year = part;
            } else if (!part.isEmpty()) {
                merchant = part;
            }
        }

        List<Invoice> filteredInvoices = InvoiceFilter.filterByMerchantMonthYear(manager.getInvoices(), merchant, month, year);

        if (filteredInvoices.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No invoices found for the given criteria.", "No Results", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        FilteredInvoicesDialog dialog = new FilteredInvoicesDialog(this, filteredInvoices);
        dialog.setVisible(true);
    }



    private String lastLoadedFilePath = null;

    private void loadFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        String filename = fileChooser.getSelectedFile().getAbsolutePath();

        if (filename.equals(lastLoadedFilePath)) {
            JOptionPane.showMessageDialog(this, "This file has already been loaded.", "Duplicate File", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> errors = processor.readFromFile(filename, manager);

        refreshTable();

        taErrors.setText("");
        if (!errors.isEmpty()) {
            for (String error : errors) {
                taErrors.append(error + "\n");
            }
            JOptionPane.showMessageDialog(this, "File loaded with errors. Check error log.", "Load Complete", JOptionPane.WARNING_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "File loaded successfully.", "Load Complete", JOptionPane.INFORMATION_MESSAGE);
        }

        if (manager.getInvoices().size() > 0) {
            btnExport.setVisible(true);
        }

        lastLoadedFilePath = filename;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Invoice inv : manager.getInvoices()) {
            tableModel.addRow(new Object[]{
                    inv.getInvoiceNumber(),
                    inv.getMerchantName(),
                    inv.getDate(),
                    inv.getAmount(),
                    inv.getTax(),
                    inv.getTotalAmount()
            });
        }
    }

    private void deleteSelectedInvoices() {
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Please select one or more invoices to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected invoice(s)?",
                "Confirm Deletion",
                JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;}

        for (int i = selectedRows.length - 1; i >= 0; i--) {
            int modelRow = table.convertRowIndexToModel(selectedRows[i]);
            int invoiceNumber = (int) tableModel.getValueAt(modelRow, 0);

            manager.deleteInvoice(invoiceNumber);

            tableModel.removeRow(modelRow);
        }

        if (manager.getInvoices().isEmpty()) {
            btnExport.setVisible(false);
        }

        JOptionPane.showMessageDialog(this, "Selected invoice(s) deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
    }

    private void exportInvoices() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) return;

        String filename = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filename.endsWith(".csv")) {
            filename += ".csv";
        }

        try (PrintWriter pw = new PrintWriter(filename, "UTF-8")) {
            pw.println("Invoice No;Merchant;Date;Amount;Tax;Total");

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pw.printf(java.util.Locale.US, "%s;\"%s\";\"%s\";%.2f;%.2f;%.2f%n",
                        tableModel.getValueAt(i, 0),
                        tableModel.getValueAt(i, 1),
                        tableModel.getValueAt(i, 2),
                        Double.parseDouble(tableModel.getValueAt(i, 3).toString()),
                        Double.parseDouble(tableModel.getValueAt(i, 4).toString()),
                        Double.parseDouble(tableModel.getValueAt(i, 5).toString())
                );
            }
            JOptionPane.showMessageDialog(this, "Invoices exported successfully.", "Export Complete", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error exporting invoices: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SplashScreen(() -> new InvoiceGUI()));

    }
}