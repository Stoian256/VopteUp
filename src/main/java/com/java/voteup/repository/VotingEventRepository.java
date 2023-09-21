package com.java.voteup.repository;

import com.java.voteup.domain.VotingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotingEventRepository extends JpaRepository<VotingEvent, Integer> {
}
