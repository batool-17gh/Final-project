package model.MangClasses;
import java.util.ArrayList;
import java.util.List;

public class InvoiceFilter {
    // This method filters a list of invoices based on merchant, month, and year
    public static List<Invoice> filterByMerchantMonthYear(List<Invoice> invoices, String merchant, String month, String year) {
        List<Invoice> filteredInvoices = new ArrayList<>();  // Create a new list to store filtered invoices

        // Loop through all invoices
        for (Invoice invoice : invoices) {
            // Check if the merchant matches or if no merchant filter is provided
            boolean merchantMatches = (merchant == null || merchant.isEmpty()) ||
                    invoice.getMerchantName().equalsIgnoreCase(merchant);

            // Check if the month matches or if no month filter is provided
            boolean monthMatches = (month == null || month.isEmpty()) ||
                    (invoice.getDate() != null && invoice.getDate().length() >= 7 &&
                            invoice.getDate().substring(5, 7).equals(month));

            // Check if the year matches or if no year filter is provided
            boolean yearMatches = (year == null || year.isEmpty()) ||
                    (invoice.getDate() != null && invoice.getDate().length() >= 4 &&
                            invoice.getDate().substring(0, 4).equals(year));

            // If all filters match, add invoice to the filtered list
            if (merchantMatches && monthMatches && yearMatches) {
                filteredInvoices.add(invoice);
            }
        }

        // Return the list of filtered invoices
        return filteredInvoices;
    }
}
