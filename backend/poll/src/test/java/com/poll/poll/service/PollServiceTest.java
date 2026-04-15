package com.poll.poll.service;

import com.poll.poll.dto.poll.PollRequest;
import com.poll.poll.dto.poll.PollResponse;
import com.poll.poll.dto.polloption.PollOptionResponse;
import com.poll.poll.entity.Poll;
import com.poll.poll.entity.PollOption;
import com.poll.poll.repository.PollOptionRepository;
import com.poll.poll.repository.PollRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PollServiceTest {

    @Mock
    private PollRepository pollRepository;

    @Mock
    private PollOptionRepository pollOptionRepository;

    @InjectMocks
    private PollService pollService;

    @BeforeEach
    void setUp() {
        // Mockito will inject mocks
    }

    @Test
    void createPoll_shouldReturnCreatedPollResponse() {
        PollRequest request = new PollRequest("Favorite color?", List.of("Red", "Blue"));

        Poll saved = new Poll();
        saved.setId(1L);
        saved.setQuestion(request.question());

        when(pollRepository.save(any(Poll.class))).thenReturn(saved);
        when(pollOptionRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PollResponse response = pollService.createPoll(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Favorite color?", response.question());
        assertEquals(2, response.options().size());

        verify(pollRepository, times(1)).save(any(Poll.class));
        verify(pollOptionRepository, times(1)).saveAll(any());
    }

    @Test
    void getPollById_shouldReturnPollResponse_whenExists() {
        Long pollId = 2L;
        Poll poll = new Poll();
        poll.setId(pollId);
        poll.setQuestion("Best language?");

        PollOption option1 = new PollOption();
        option1.setId(11L);
        option1.setOptionText("Java");
        option1.setVoteCount(3);
        option1.setPoll(poll);

        PollOption option2 = new PollOption();
        option2.setId(12L);
        option2.setOptionText("Python");
        option2.setVoteCount(5);
        option2.setPoll(poll);

        when(pollRepository.findById(pollId)).thenReturn(Optional.of(poll));
        when(pollOptionRepository.findAllByPollId(pollId)).thenReturn(List.of(option1, option2));

        PollResponse response = pollService.getPollById(pollId);

        assertNotNull(response);
        assertEquals(pollId, response.id());
        assertEquals("Best language?", response.question());
        assertEquals(2, response.options().size());

        PollOptionResponse o1 = response.options().get(0);
        assertEquals(11L, o1.optionId());
        assertEquals("Java", o1.optionText());
        assertEquals(3, o1.voteCount());
    }

    @Test
    void getPollById_shouldThrow_whenNotFound() {
        when(pollRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pollService.getPollById(99L));
        assertEquals("404 NOT_FOUND \"Poll not found\"", ex.getMessage());
    }

    @Test
    void vote_shouldIncrementAndReturnOption() {
        Long optionId = 21L;

        PollOption option = new PollOption();
        option.setId(optionId);
        option.setOptionText("Option A");
        option.setVoteCount(7);

        // incrementVoteCount doesn't return value; we ensure it's called
        doNothing().when(pollOptionRepository).incrementVoteCount(optionId);
        doNothing().when(pollOptionRepository).flush();
        when(pollOptionRepository.findById(optionId)).thenReturn(Optional.of(option));

        PollOptionResponse response = pollService.vote(optionId);

        assertNotNull(response);
        assertEquals(optionId, response.optionId());
        assertEquals("Option A", response.optionText());
        assertEquals(7, response.voteCount());

        verify(pollOptionRepository, times(1)).incrementVoteCount(optionId);
        verify(pollOptionRepository, times(1)).flush();
        verify(pollOptionRepository, times(1)).findById(optionId);
    }

    @Test
    void vote_shouldThrow_whenOptionNotFound() {
        Long optionId = 99L;
        doNothing().when(pollOptionRepository).incrementVoteCount(optionId);
        doNothing().when(pollOptionRepository).flush();
        when(pollOptionRepository.findById(optionId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> pollService.vote(optionId));
        assertEquals("404 NOT_FOUND \"Poll option not found\"", ex.getMessage());

        verify(pollOptionRepository, times(1)).incrementVoteCount(optionId);
        verify(pollOptionRepository, times(1)).flush();
        verify(pollOptionRepository, times(1)).findById(optionId);
    }

}
