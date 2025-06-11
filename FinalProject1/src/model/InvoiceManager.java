package model;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InvoiceManager {
    private List<Invoice> invoices;
    private int maxSize; // todo

    public InvoiceManager(int maxSize) {
        this.maxSize = maxSize;
        this.invoices = new ArrayList<>();
    }

    public boolean addInvoice(Invoice invoice) {
        if (invoices.size() >= maxSize) return false;
        invoices.add(invoice);
        return true;
    }

    public Invoice getInvoiceByNumber(int number) {
        for (Invoice inv : invoices) {
            if (inv.getInvoiceNumber() == number) return inv;
        }
        return null;
    }

    public boolean deleteInvoice(int number) {
        return invoices.removeIf(inv -> inv.getInvoiceNumber() == number);
    }
    public void deleteAllInvoices() {
        invoices.clear();
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public double calculateTotalByMerchant(String merchant) {
        return invoices.stream()
                .filter(i -> i.getMerchantName().equalsIgnoreCase(merchant))
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    public double calculateTotalByMonth(String month) {
        return invoices.stream()
                .filter(i -> i.getDate().substring(5, 7).equals(month))
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    public double calculateTotalByMerchantAndMonth(String merchant, String month) {
        return invoices.stream()
                .filter(i -> i.getMerchantName().equalsIgnoreCase(merchant)
                        && i.getDate().substring(5, 7).equals(month))
                .mapToDouble(Invoice::getTotalAmount)
                .sum();
    }

    public List<Invoice> filterByMerchant(String merchant) {
        return invoices.stream()
                .filter(i -> i.getMerchantName().equalsIgnoreCase(merchant))
                .collect(Collectors.toList());
    }

    public List<Invoice> filterByMonth(String month) {
        return invoices.stream()
                .filter(i -> i.getDate().substring(5, 7).equals(month))
                .collect(Collectors.toList());
    }

    public List<Invoice> filterByMerchantAndMonth(String merchant, String month) {
        return invoices.stream()
                .filter(i -> i.getMerchantName().equalsIgnoreCase(merchant)
                        && i.getDate().substring(5, 7).equals(month))
                .collect(Collectors.toList());
    }
//    public List<Invoice> getInvoicesByMerchant(String merchant) {
//        List<Invoice> results = new ArrayList<>();
//        for (Invoice inv : invoices) {
//            if (inv.getMerchantName().equalsIgnoreCase(merchant)) {
//                results.add(inv);
//            }
//        }
//        return results;
//    }

//    public List<Invoice> getInvoicesByMonth(String month) {
//        List<Invoice> results = new ArrayList<>();
//        for (Invoice inv : invoices) {
//            if (inv.getDate().substring(5, 7).equals(month)) {
//                results.add(inv);
//            }
//        }
//        return results;
//    }

//    public List<Invoice> getInvoicesByMerchantAndMonth(String merchant, String month) {
//        List<Invoice> results = new ArrayList<>();
//        for (Invoice inv : invoices) {
//            if (inv.getMerchantName().equalsIgnoreCase(merchant) && inv.getDate().substring(5, 7).equals(month)) {
//                results.add(inv);
//            }
//        }
//        return results;
//    }

}
