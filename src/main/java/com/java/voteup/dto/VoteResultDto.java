package com.java.voteup.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@EqualsAndHashCode
public class VoteResultDto {
    private Integer optionId;
    private String optionName;
    private Integer votes;
}
