package com.Swathi.tnbbank.dto;

import lombok.*;




@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BankResponse {

    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;

}
