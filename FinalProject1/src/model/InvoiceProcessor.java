package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceProcessor {

    // This method reads from a file and adds valid invoices to the manager
    // It returns a list of error messages for invalid lines
    public List<String> readFromFile(String filename, InvoiceManager manager) {
        List<String> errors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split(",");
                if (parts.length != 5) {
                    errors.add("Line " + lineNumber + ": Expected 5 fields.");
                    continue;
                }
                try {
                    int number = Integer.parseInt(parts[0].trim());
                    String merchant = parts[1].trim();
                    String date = parts[2].trim();
                    double amount = Double.parseDouble(parts[3].trim());
                    double tax = Double.parseDouble(parts[4].trim());

                    if (merchant.isEmpty() || !merchant.matches("[a-zA-Z ]+")) {
                        errors.add("Line " + lineNumber + ": Invalid merchant name. Only letters and spaces are allowed.");
                        continue;
                    }
                    if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        errors.add("Line " + lineNumber + ": Invalid date format. Expected YYYY-MM-DD.");
                        continue;
                    }
                    if (amount <= 0 || tax < 0) {
                        errors.add("Line " + lineNumber + ": Amount must be > 0 and tax must be >= 0.");
                        continue;
                    }
                    if (manager.getInvoiceByNumber(number) != null) {
                        errors.add("Line " + lineNumber + ": Invoice number already exists.");
                        continue;
                    }

                    Invoice invoice = new Invoice(number, merchant, date, amount, tax);
                    manager.addInvoice(invoice);

                } catch (NumberFormatException e) {
                    errors.add("Line " + lineNumber + ": Number format error.");

                }
            }
        } catch (IOException e) {
            errors.add("Error reading file: " + e.getMessage());
        }
        return errors;
    }
}
