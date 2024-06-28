package com.Swathi.tnbbank.repository;

import com.Swathi.tnbbank.dto.BankResponse;
import com.Swathi.tnbbank.dto.CreditDebitRequest;
import com.Swathi.tnbbank.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    User findByAccountNumber(String accountNumber);


}
