package model;

public class Invoice {
    private int invoiceNumber;
    private String merchantName;
    private String date;
    private double amount;
    private double tax;

    public Invoice(int invoiceNumber, String merchantName, String date, double amount, double tax) {
        this.invoiceNumber = invoiceNumber;
        this.merchantName = merchantName;
        this.date = date;
        this.amount = amount;
        this.tax = tax;
    }

    public int getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public String getDate() {
        return date;
    }

    public double getAmount() {
        return amount;
    }

    public double getTax() {
        return tax;
    }

    public double getTotalAmount() {
        return amount + tax;
    }

    public String toFileString() {
        return String.format("%d,%s,%s,%.2f,%.2f", invoiceNumber, merchantName, date, amount, tax);
    }

    @Override
    public String toString() {
        return String.format("Invoice #%d | %s | %s | %.2f + %.2f = %.2f",
                invoiceNumber, merchantName, date, amount, tax, getTotalAmount());
    }
}