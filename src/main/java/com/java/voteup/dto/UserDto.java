package com.java.voteup.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class UserDto {
    private String firstName;
    private String lastName;
    private String cnp;
    private String email;
}
