package com.poll.poll.dto.polloption;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record PollOptionResponse(
        Long optionId,

        @NotBlank(message = "The option text cannot be blank")
        String optionText,
        int voteCount
        //eventually add vote here?

) {

}
