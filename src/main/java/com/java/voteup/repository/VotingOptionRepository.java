package com.java.voteup.repository;

import com.java.voteup.domain.VotingOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VotingOptionRepository extends JpaRepository<VotingOption, Integer> {
    VotingOption findByIdEquals(Integer id);
    List<VotingOption> findByVotingEvent_IdEquals(Integer id);

}
