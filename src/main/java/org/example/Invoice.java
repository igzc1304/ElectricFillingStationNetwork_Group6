package org.example;

import java.time.LocalDateTime;
import java.util.Objects;

public class Invoice {
    private final String invoiceNumber;
    private final LocalDateTime issueDate;
    private final Double totalAmount;
    private final EvDriver evDriver;

    Invoice(String invoiceNumber, LocalDateTime issueDate, Double totalAmount, EvDriver evDriver) {
        this.invoiceNumber = invoiceNumber;
        this.issueDate = issueDate;
        this.totalAmount = totalAmount;
        this.evDriver = evDriver;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDateTime getIssueDate() {
        return issueDate;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public EvDriver getEvDriver() {
        return evDriver;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceNumber, invoice.invoiceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(invoiceNumber);
    }

    @Override
    public String toString() {
        return "Invoice{" + "\n" +
                "\tinvoiceNumber='" + invoiceNumber + '\'' + "\n" +
                "\tissueDate=" + issueDate + "\n" +
                "\ttotalAmount=" + totalAmount + "\n" +
                '}';
    }
}
