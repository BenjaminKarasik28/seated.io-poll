package com.poll.poll.dto.poll;

import com.poll.poll.dto.polloption.PollOptionResponse;
import com.poll.poll.entity.PollOption;
import lombok.Builder;

import java.util.List;

@Builder
public record PollResponse(
        Long id,
        String question,
        List<PollOptionResponse> options
) {}