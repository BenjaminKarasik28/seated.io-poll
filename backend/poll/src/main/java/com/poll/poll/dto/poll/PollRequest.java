package com.poll.poll.dto.poll;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record PollRequest(

        @NotBlank(message = "The question cannot be blank")
        String question,

        @Size(min = 2, max = 10, message = "The poll must have between 2 and 10 options")
        List<String> options
) {}
