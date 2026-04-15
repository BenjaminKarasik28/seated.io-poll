package com.poll.poll.controller;

import com.poll.poll.dto.poll.PollResponse;
import com.poll.poll.dto.polloption.PollOptionResponse;
import com.poll.poll.service.PollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PollControllerTest {

    private MockMvc mockMvc;
    private PollService pollService;

    @BeforeEach
    void setUp() {
        pollService = mock(PollService.class);
        PollController controller = new PollController(pollService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createPoll_shouldReturnCreated() throws Exception {
        String json = "{\"question\":\"Favorite color?\",\"options\":[\"Red\",\"Blue\"]}";

        when(pollService.createPoll(any())).thenReturn(
                new PollResponse(1L, "Favorite color?", List.of(
                        new PollOptionResponse(11L, "Red", 0),
                        new PollOptionResponse(12L, "Blue", 0)
                ))
        );

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.options", hasSize(2)));
    }

    @Test
    void getPollById_shouldReturnAccepted() throws Exception {
        Long id = 5L;
        when(pollService.getPollById(id)).thenReturn(
                new PollResponse(id, "Best?", List.of(
                        new PollOptionResponse(21L, "A", 1)
                ))
        );

        mockMvc.perform(get("/poll/{id}", id))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.options", hasSize(1)));
    }

    @Test
    void vote_shouldReturnAccepted() throws Exception {
        Long optionId = 7L;
        when(pollService.vote(optionId)).thenReturn(new PollOptionResponse(optionId, "Opt", 2));

        mockMvc.perform(post("/poll/{id}/vote", optionId))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.optionId").value(7))
                .andExpect(jsonPath("$.voteCount").value(2));
    }
}
