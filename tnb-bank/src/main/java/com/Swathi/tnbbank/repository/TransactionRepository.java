package com.Swathi.tnbbank.repository;

import com.Swathi.tnbbank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

}
