package com.java.voteup.service;

import com.java.voteup.domain.*;
import com.java.voteup.dto.VoteResultDto;
import com.java.voteup.repository.VotingEventRepository;
import com.java.voteup.repository.VotingOptionRepository;
import com.java.voteup.repository.VotingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VotingService {
    private final VotingRepository votingRepository;
    private final VotingEventRepository votingEventRepository;
    private final VotingOptionRepository votingOptionRepository;
    Chain blockchain = new Chain(4);

    @PostConstruct
    public void initializeBlockchain() {
        List<Vote> votes = votingRepository.findAll();
        for (Vote vote : votes) {
            blockchain.addBlock(new Block(vote, blockchain.getLatestBlock().getHash()));
        }
    }

    public List<VotingOption> findAllOptions() {
        List<VotingOption> votingOptions = votingOptionRepository.findAll();
        return votingOptions;
    }

    public List<VotingOption> findByVotingEventId(Integer eventId) {
        return votingOptionRepository.findByVotingEvent_IdEquals(eventId);
    }

    public List<VotingEvent> findAllEvents() {
        return votingEventRepository.findAll();
    }

    public String submitVote(Vote vote) {
        if (votingRepository.findByUserIdEqualsAndEventIdEquals(vote.getUserId(), vote.getEventId()) != null)
            return "Nu puteti vota de doua ori in cadrul aceluiasi eveniment";
        blockchain.addBlock(new Block(vote, blockchain.getLatestBlock().getHash()));
        votingRepository.save(vote);
        return "Votul a fost inregistrat cu succes";
    }

    public List<VoteResultDto> getResults(Integer eventId) throws Exception {
        if (!blockchain.isChainValid(votingRepository.findAll().stream().filter(t -> Objects.equals(t.getEventId(), eventId)).toList(), eventId)) {
            throw new Exception("Voturile au fost corupte!");
        }
        return votingRepository.findAll().stream()
                .filter(t -> Objects.equals(t.getEventId(), eventId))
                .collect(Collectors.groupingBy(t -> t.getOptionId(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> new VoteResultDto(entry.getKey(), votingOptionRepository.findByIdEquals(entry.getKey()).getName(), entry.getValue().intValue()))
                .collect(Collectors.toList());
    }
}
