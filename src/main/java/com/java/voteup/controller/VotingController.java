package com.java.voteup.controller;

import com.java.voteup.domain.Vote;
import com.java.voteup.domain.VotingEvent;
import com.java.voteup.domain.VotingOption;
import com.java.voteup.dto.VoteResultDto;
import com.java.voteup.service.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class VotingController {
    private final VotingService votingService;

    @CrossOrigin
    @PostMapping("/submit-vote")
    public ResponseEntity<String> registerUser(@RequestBody Vote request) {
        String result = votingService.submitVote(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/voting-options")
    public List<VotingOption> findAllOptions() {
        return votingService.findAllOptions();
    }


    @GetMapping("/voting-options-event")
    public List<VotingOption> findByVotingEventId(@RequestParam("eventId") Integer eventId) {
        return votingService.findByVotingEventId(eventId);
    }

    @GetMapping("/voting-events")
    public List<VotingEvent> findAllEvents() {
        return votingService.findAllEvents();
    }

    @GetMapping("/results")
    public List<VoteResultDto> getResults(@RequestParam("eventId") Integer eventId) {
        try {
            return votingService.getResults(eventId);
        } catch (Exception e) {
            return null;
        }
    }
}
