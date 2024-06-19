package com.Swathi.tnbbank.services.impl;

import com.Swathi.tnbbank.dto.TransactionDto;
import com.Swathi.tnbbank.entity.Transaction;

public interface TransactionService {

    void saveTransaction(TransactionDto transactionDto);
}
