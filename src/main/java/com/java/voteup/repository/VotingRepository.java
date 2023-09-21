package com.java.voteup.repository;

import com.java.voteup.domain.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingRepository extends JpaRepository<Vote, Integer> {
    Vote findByUserIdEqualsAndEventIdEquals(Integer userId, Integer eventId);
}
