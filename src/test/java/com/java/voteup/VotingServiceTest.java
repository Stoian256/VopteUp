package com.java.voteup;

import com.java.voteup.domain.Vote;
import com.java.voteup.domain.VotingEvent;
import com.java.voteup.domain.VotingOption;
import com.java.voteup.dto.VoteResultDto;
import com.java.voteup.repository.VotingEventRepository;
import com.java.voteup.repository.VotingOptionRepository;
import com.java.voteup.repository.VotingRepository;
import com.java.voteup.service.VotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@Disabled
class VotingServiceTest {
    @Mock
    private VotingRepository votingRepository;

    @Mock
    private VotingEventRepository votingEventRepository;

    @Mock
    private VotingOptionRepository votingOptionRepository;

    private VotingService votingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        votingService = new VotingService(votingRepository, votingEventRepository, votingOptionRepository);
    }

    @Test
    void testFindAllOptions() {
        // Mock data
        List<VotingOption> options = new ArrayList<>();
        options.add(new VotingOption(1,"Option 1",null));
        options.add(new VotingOption(2,"Option 2",null));
        when(votingOptionRepository.findAll()).thenReturn(options);

        // Test
        List<VotingOption> result = votingService.findAllOptions();

        // Verify
        assertEquals(options, result);
        verify(votingOptionRepository, times(1)).findAll();
    }

    @Test
    void testFindByVotingEventId() {
        // Mock data
        Integer eventId = 1;
        List<VotingOption> options = new ArrayList<>();
        options.add(new VotingOption(1,"Option 1",null));
        options.add(new VotingOption(2,"Option 2",null));
        when(votingOptionRepository.findByVotingEvent_IdEquals(eventId)).thenReturn(options);

        // Test
        List<VotingOption> result = votingService.findByVotingEventId(eventId);

        // Verify
        assertEquals(options, result);
        verify(votingOptionRepository, times(1)).findByVotingEvent_IdEquals(eventId);
    }

    @Test
    void testFindAllEvents() {
        // Mock data
        List<VotingEvent> events = new ArrayList<>();
        events.add(new VotingEvent(1,"Event 1", null));
        events.add(new VotingEvent(2,"Event 2",null));
        when(votingEventRepository.findAll()).thenReturn(events);

        // Test
        List<VotingEvent> result = votingService.findAllEvents();

        // Verify
        assertEquals(events, result);
        verify(votingEventRepository, times(1)).findAll();
    }

    @Test
    void testSubmitVote() {
        // Mock data
        Vote vote = new Vote(1, 1, 1);
        when(votingRepository.findByUserIdEqualsAndEventIdEquals(vote.getUserId(), vote.getEventId())).thenReturn(null);

        // Test
        String result = votingService.submitVote(vote);

        // Verify
        assertEquals("Votul a fost inregistrat cu succes", result);
        verify(votingRepository, times(1)).findByUserIdEqualsAndEventIdEquals(vote.getUserId(), vote.getEventId());
        verify(votingRepository, times(1)).save(vote);
    }

    @Test
    void testSubmitVote_DuplicateVote() {
        // Mock data
        Vote vote = new Vote(1, 1, 1);
        when(votingRepository.findByUserIdEqualsAndEventIdEquals(vote.getUserId(), vote.getEventId())).thenReturn(vote);

        // Test
        String result = votingService.submitVote(vote);

        // Verify
        assertEquals("Nu puteti vota de doua ori in cadrul aceluiasi eveniment", result);
        verify(votingRepository, times(1)).findByUserIdEqualsAndEventIdEquals(vote.getUserId(), vote.getEventId());
        verify(votingRepository, never()).save(vote);
    }
}
