package com.Swathi.tnbbank.services.impl;

import com.Swathi.tnbbank.dto.EmailDetails;
import com.Swathi.tnbbank.entity.Transaction;
import com.Swathi.tnbbank.entity.User;
import com.Swathi.tnbbank.repository.TransactionRepository;
import com.Swathi.tnbbank.repository.UserRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@AllArgsConstructor
@Slf4j
public class BankStatement {

    @Autowired
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;
    private EmailService emailService;

    private static final String FILE = "C:\\Users\\lenovo\\OneDrive\\Attachments\\MyStatement.pdf";


    //Retrieve list of transactions within a data range given an account number
    // generate a pdf file of transactions
    // send the file via email

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate) throws DocumentException, FileNotFoundException {
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList;
        transactionList = transactionRepository.findAll().stream().filter(transaction -> transaction.getAccountNumber().equals(accountNumber))
                .filter(transaction -> transaction.getCreatedAt().isEqual(start)).filter(transaction -> transaction.getCreatedAt().isEqual(end)).toList();
        User user = userRepository.findByAccountNumber(accountNumber);
        String customerName = user.getFirstName() + " "+user.getLastName();

        //to provide the size
        Rectangle statementSize = new Rectangle(PageSize.A4);
        Document document = new Document(statementSize);
        log.info("setting size of document");
        OutputStream outputStream = new FileOutputStream(FILE);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        PdfPTable bankInfoTable = new PdfPTable(1);
        PdfPCell bankName = new PdfPCell(new Phrase("Swathi tnb Bank"));
        bankName.setBorder(0);
        bankName.setBackgroundColor(BaseColor.RED);
        bankName.setPadding(20f);

        //addresss of the company
        PdfPCell bankAddress = new PdfPCell(new Phrase("F6, Miami street, Chennai - 600087"));
        bankAddress.setBorder(0);
        //adding into the pdf
        bankInfoTable.addCell(bankName);
        bankInfoTable.addCell(bankAddress);

        PdfPTable statementInfo = new PdfPTable(2);
        PdfPCell customerInfo = new PdfPCell(new Phrase("Start Date: " + startDate));
        customerInfo.setBorder(0);
        PdfPCell statement = new PdfPCell(new Phrase("STATEMENT OF ACCOUNT"));
        statement.setBorder(0);
        PdfPCell stopDate = new PdfPCell(new Phrase("End Date: "+endDate));
        stopDate.setBorder(0);
        PdfPCell name = new PdfPCell(new Phrase("Customer Name: "+ customerName));
        name.setBorder(0);
        PdfPCell space = new PdfPCell();
        PdfPCell address = new PdfPCell(new Phrase("Customer Address "+user.getAddress()));
        address.setBorder(0);

        PdfPTable transactionsTable = new PdfPTable(4);
        PdfPCell date = new PdfPCell(new Phrase("DATE"));
        date.setBackgroundColor(BaseColor.RED);
        date.setBorder(0);
        PdfPCell transactionType = new PdfPCell(new Phrase("TRANSACTION TYPE"));
        transactionType.setBackgroundColor(BaseColor.RED);
        transactionType.setBorder(0);
        PdfPCell transactionAmount = new PdfPCell(new Phrase("TRANSACTION AMOUNT"));
        transactionType.setBackgroundColor(BaseColor.RED);
        transactionType.setBorder(0);
        PdfPCell status = new PdfPCell(new Phrase("STATUS"));
        transactionType.setBackgroundColor(BaseColor.RED);
        transactionType.setBorder(0);

        transactionsTable.addCell(date);
        transactionsTable.addCell(transactionType);
        transactionsTable.addCell(transactionAmount);
        transactionsTable.addCell(status);

        transactionList.forEach(transaction -> {
            transactionsTable.addCell(new Phrase(transaction.getCreatedAt().toString()));
            transactionsTable.addCell(new Phrase(transaction.getTransactionType().toString()));
            transactionsTable.addCell(new Phrase(transaction.getAmount().toString()));
            transactionsTable.addCell(new Phrase(transaction.getStatus().toString()));
        });

        statementInfo.addCell(customerInfo);
        statementInfo.addCell(statement);
        statementInfo.addCell(endDate);
        statementInfo.addCell(name);
        statementInfo.addCell(address);

        document.add(bankInfoTable);
        document.add(statementInfo);
        document.add(transactionsTable);

        document.close();

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(user.getEmail())
                .subject("STATEMENT OF ACCOUNT")
                .messageBody("Kindly find your account statement attached!")
                .attachment(FILE)
                .build();

        emailService.sendEmailWithAttachment(emailDetails);



        return transactionList;

    }
}

