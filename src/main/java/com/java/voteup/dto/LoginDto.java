package com.java.voteup.dto;

import lombok.*;

import java.math.BigInteger;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class LoginDto {
    private String username;
    private BigInteger c;
    private BigInteger zx;
}