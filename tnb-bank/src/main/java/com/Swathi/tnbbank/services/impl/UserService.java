package com.Swathi.tnbbank.services.impl;

import com.Swathi.tnbbank.dto.*;

public interface UserService {

    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
    BankResponse creditAccount(CreditDebitRequest request);
    BankResponse debitAccount(CreditDebitRequest request);
    BankResponse transferAccount(TransferRequest request);
    BankResponse login(LoginDto loginDto);
}
