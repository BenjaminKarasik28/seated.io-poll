package com.poll.poll.dto.polloption;

import lombok.Builder;

@Builder
public record PollOptionResponse(
        Long optionId,
        String optionText,
        int voteCount
        //eventually add vote here?

) {

}
