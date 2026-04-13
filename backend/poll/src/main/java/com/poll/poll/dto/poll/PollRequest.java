package com.poll.poll.dto.poll;


import lombok.Builder;

import java.util.List;

@Builder
public record PollRequest(
        String question,
        List<String> options
) {}
