import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

class FirmInfo {
    private String name;
    private String address;
    private String contactNumber;

    public FirmInfo(String name, String address, String contactNumber) {
        this.name = name;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    @Override
    public String toString() {
        return "Firm Name: " + name + "\n" +
                "Address: " + address + "\n" +
                "Contact Number: " + contactNumber;
    }
}

class Bill {
    private static int nextBillNumber = 1;

    private int billNumber;
    private String[] items;

    public Bill(String[] items) {
        this.billNumber = nextBillNumber++;
        this.items = items;
    }

    public int getBillNumber() {
        return billNumber;
    }

    public String[] getItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder billString = new StringBuilder();
        billString.append("Bill Number: ").append(billNumber).append("\n");
        for (int i = 0; i < items.length; i++) {
            billString.append("Item ").append(i + 1).append(": ").append(items[i]).append("\n");
        }
        return billString.toString();
    }
}

class DailySalesReport {
    private static List<Bill> bills = new ArrayList<>();

    public static void saveBill(Bill bill) {
        bills.add(bill);
    }

    public static List<Bill> getBills() {
        return bills;
    }
}

public class BillingUI extends JFrame implements ActionListener {
    private JLabel srNoLabel, nameLabel, itemCodeLabel, quantityLabel, priceLabel, taxesLabel;
    private JTextField srNoField, nameField, itemCodeField, quantityField, priceField, taxesField;
    private JButton calculateButton, printButton;
    private JTextArea resultArea;

    private FirmInfo firmInfo;

    public BillingUI() {
        setTitle("Billing Software");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize firm information
        firmInfo = new FirmInfo("Vijay sales", "123 Main St, City", "123-456-7890");

        JPanel inputPanel = new JPanel(new GridLayout(6, 2, 5, 5));
        srNoLabel = new JLabel("Sr No:");
        nameLabel = new JLabel("Item/Particulars:");
        itemCodeLabel = new JLabel("Item Code:");
        quantityLabel = new JLabel("Quantity:");
        priceLabel = new JLabel("Price:");
        taxesLabel = new JLabel("Tax(percentage):");

        srNoField = new JTextField();
        nameField = new JTextField();
        itemCodeField = new JTextField();
        quantityField = new JTextField();
        priceField = new JTextField();
        taxesField = new JTextField();

        inputPanel.add(srNoLabel);
        inputPanel.add(srNoField);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(itemCodeLabel);
        inputPanel.add(itemCodeField);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantityField);
        inputPanel.add(priceLabel);
        inputPanel.add(priceField);
        inputPanel.add(taxesLabel);
        inputPanel.add(taxesField);

        calculateButton = new JButton("Calculate Total");
        calculateButton.addActionListener(this);

        printButton = new JButton("Print");
        printButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(calculateButton);
        buttonPanel.add(printButton);

        resultArea = new JTextArea(10, 20);
        resultArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultArea);

        Container container = getContentPane();
        container.setLayout(new BorderLayout());
        container.add(inputPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
        container.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == calculateButton) {
            calculateTotal();
        } else if (e.getSource() == printButton) {
            printBill();
        }
    }

    private void calculateTotal() {
        try {
            int srNo = Integer.parseInt(srNoField.getText());
            String name = nameField.getText();
            String itemCode = itemCodeField.getText();
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            double taxes = Double.parseDouble(taxesField.getText());

            double totalWithoutTaxes = quantity * price;
            double taxAmount=totalWithoutTaxes * (taxes/100);
            double totalWithTaxes = totalWithoutTaxes + taxAmount;

            DecimalFormat decimalFormat = new DecimalFormat("#.##");

            // Display bill details
            resultArea.setText("Sr No: " + srNo + "\n" +
                    "Item/Particulars: " + name + "\n" +
                    "Item Code: " + itemCode + "\n" +
                    "Quantity: " + quantity + "\n" +
                    "Price: " + price + "\n" +
                    "Tax(Percentage): " + taxes + "\n" +
                    "Total Without taxes: " + decimalFormat.format(totalWithoutTaxes) + "\n" +
                    "Bill total(with GST tax): " + decimalFormat.format(totalWithTaxes));

            // Save bill in daily sales report
            String[] items = {name, itemCode, Integer.toString(quantity), Double.toString(price), Double.toString(taxes)};
            Bill bill = new Bill(items);
            DailySalesReport.saveBill(bill);

        } catch (NumberFormatException ex) {
            resultArea.setText("Please enter valid numeric values.");
        }
    }

    private void printBill() {
        try {
            StringBuilder report = new StringBuilder();
            report.append(firmInfo.toString()).append("\n\n");
            report.append(resultArea.getText());
            JOptionPane.showMessageDialog(this, report.toString(), "Print Bill", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error printing bill: " + e.getMessage(), "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BillingUI billingUI = new BillingUI();
            billingUI.setVisible(true);
     
        });
    }
}
