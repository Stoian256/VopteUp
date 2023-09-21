package com.java.voteup.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class VoteId implements Serializable {
    private Integer userId;
    private Integer eventId;
}

