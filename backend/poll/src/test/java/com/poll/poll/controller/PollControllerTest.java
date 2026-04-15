package com.poll.poll.controller;

import com.poll.poll.dto.poll.PollResponse;
import com.poll.poll.dto.polloption.PollOptionResponse;
import com.poll.poll.service.PollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.server.ResponseStatusException;

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
    private PollController controller;

    @BeforeEach
    void setUp() {
        pollService = mock(PollService.class);
        controller = new PollController(pollService);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setValidator(validator).build();
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
    void createPoll_shouldReturnBadRequest_whenQuestionBlank() throws Exception {
        String json = "{\"question\":\"\",\"options\":[\"A\",\"B\"]}";

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createPoll_shouldReturnBadRequest_whenTooFewOptions() throws Exception {
        String json = "{\"question\":\"Q?\",\"options\":[\"Only\"]}";

        mockMvc.perform(post("/poll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getPollById_shouldReturnNotFound_whenServiceThrows() throws Exception {
        Long id = 9L;
        when(pollService.getPollById(id)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll not found"));

        mockMvc.perform(get("/poll/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void vote_shouldReturnNotFound_whenServiceThrows() throws Exception {
        Long optionId = 77L;
        when(pollService.vote(optionId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Poll option not found"));

        mockMvc.perform(post("/poll/{id}/vote", optionId))
                .andExpect(status().isNotFound());
    }
}
