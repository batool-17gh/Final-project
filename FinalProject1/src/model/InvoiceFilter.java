package model;

import java.util.ArrayList;
import java.util.List;

public class InvoiceFilter {

//    /**
//     * Filters invoices by merchant name, month, and year.
//     * Any of the filters can be null or empty to skip filtering by that field.
//     *
//     * @param invoices The list of invoices to filter.
//     * @param merchant The merchant name to filter by (case insensitive). Can be null or empty.
//     * @param month    The month to filter by (format: "01" to "12"). Can be null or empty.
//     * @param year     The year to filter by (format: "2024", "2023", etc.). Can be null or empty.
//     * @return A list of invoices that match the provided filters.
//     */
    public static List<Invoice> filterByMerchantMonthYear(List<Invoice> invoices, String merchant, String month, String year) {
        List<Invoice> filteredInvoices = new ArrayList<>();

        for (Invoice invoice : invoices) {
            boolean merchantMatches = (merchant == null || merchant.isEmpty()) ||
                    invoice.getMerchantName().equalsIgnoreCase(merchant);

            boolean monthMatches = (month == null || month.isEmpty()) ||
                    (invoice.getDate() != null && invoice.getDate().length() >= 7 &&
                            invoice.getDate().substring(5, 7).equals(month));

            boolean yearMatches = (year == null || year.isEmpty()) ||
                    (invoice.getDate() != null && invoice.getDate().length() >= 4 &&
                            invoice.getDate().substring(0, 4).equals(year));

            if (merchantMatches && monthMatches && yearMatches) {
                filteredInvoices.add(invoice);
            }
        }

        return filteredInvoices;
    }
}
