package com.poll.poll.controller;

import com.poll.poll.PollApplication;
import com.poll.poll.dto.poll.PollRequest;
import com.poll.poll.dto.poll.PollResponse;
import com.poll.poll.service.PollService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class PollController {


    private final PollService pollService;

    public PollController(PollService pollService) {
        this.pollService = pollService;
    }

    @PostMapping("/poll")
    public ResponseEntity<PollResponse> createPoll(
            @Valid @RequestBody PollRequest request) {

        PollResponse response = pollService.createPoll(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }
    @GetMapping("/poll/{id}")
    public ResponseEntity<PollResponse> getPollById(@PathVariable Long id) {

        PollResponse response = pollService.getPollById(id);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
