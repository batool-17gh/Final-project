package model.MangClasses;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceProcessor {

    public List<String> readFromFile(String filename, InvoiceManager manager) {
        List<String> errors = new ArrayList<>();  // To collect error messages

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int lineNumber = 0;

            // Read the file line by line
            while ((line = br.readLine()) != null) {
                lineNumber++;
                String[] parts = line.split(","); // Split CSV line into parts

                // Check if the line has exactly 5 fields
                if (parts.length != 5) {
                    errors.add("Line " + lineNumber + ": Expected 5 fields.");
                    continue; // Skip this line
                }

                try {
                    // Parse each part with trimming spaces
                    int number = Integer.parseInt(parts[0].trim());
                    String merchant = parts[1].trim();
                    String date = parts[2].trim();
                    double amount = Double.parseDouble(parts[3].trim());
                    double tax = Double.parseDouble(parts[4].trim());

                    // Validate merchant name: only letters and spaces allowed
                    if (merchant.isEmpty() || !merchant.matches("[a-zA-Z ]+")) {
                        errors.add("Line " + lineNumber + ": Invalid merchant name. Only letters and spaces are allowed.");
                        continue;
                    }

                    // Validate date format YYYY-MM-DD
                    if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        errors.add("Line " + lineNumber + ": Invalid date format. Expected YYYY-MM-DD.");
                        continue;
                    }

                    // Validate amounts: amount > 0, tax >= 0
                    if (amount <= 0 || tax < 0) {
                        errors.add("Line " + lineNumber + ": Amount must be > 0 and tax must be >= 0.");
                        continue;
                    }

                    // Check if invoice number already exists
                    if (manager.getInvoiceByNumber(number) != null) {
                        errors.add("Line " + lineNumber + ": Invoice number already exists.");
                        continue;
                    }

                    // If all validations passed, create and add the invoice
                    Invoice invoice = new Invoice(number, merchant, date, amount, tax);
                    manager.addInvoice(invoice);

                } catch (NumberFormatException e) {
                    // Handle parsing errors for number and double fields
                    errors.add("Line " + lineNumber + ": Number format error.");
                }
            }
        } catch (IOException e) {
            // Handle file reading errors
            errors.add("Error reading file: " + e.getMessage());
        }

        return errors; // Return list of errors (empty if no errors)
    }
}
