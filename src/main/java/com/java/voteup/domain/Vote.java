package com.java.voteup.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "vote")
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VoteId.class)
public class Vote {
    @Id
    private Integer userId;
    @Id
    private Integer eventId;
    private Integer optionId;
}
