package model.MangClasses;
import java.util.ArrayList;
import java.util.List;

public class InvoiceManager {
    // List to store all invoices
    private List<Invoice> invoices;

    // Maximum number of invoices allowed
    private int maxSize;

    // Constructor to set the max size and initialize the list
    public InvoiceManager(int maxSize) {
        this.maxSize = maxSize;
        this.invoices = new ArrayList<>();
    }

    // Adds an invoice if maxSize is not reached yet
    public boolean addInvoice(Invoice invoice) {
        if (invoices.size() >= maxSize) {
            return false; // Cannot add more invoices
        }
        invoices.add(invoice);
        return true; // Invoice added successfully
    }

    // Search for an invoice by its number
    public Invoice getInvoiceByNumber(int number) {
        for (Invoice inv : invoices) {
            if (inv.getInvoiceNumber() == number) {
                return inv; // Found the invoice
            }
        }
        return null; // Invoice not found
    }

    // Delete an invoice by its number
    public boolean deleteInvoice(int number) {
        return invoices.removeIf(inv -> inv.getInvoiceNumber() == number);
    }

    // Get the list of all invoices
    public List<Invoice> getInvoices() {
        return invoices;
    }
}
