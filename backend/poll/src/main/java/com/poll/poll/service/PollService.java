package com.poll.poll.service;


import com.poll.poll.dto.poll.PollRequest;
import com.poll.poll.dto.poll.PollResponse;
import com.poll.poll.dto.polloption.PollOptionResponse;
import com.poll.poll.entity.Poll;
import com.poll.poll.entity.PollOption;
import com.poll.poll.repository.PollOptionRepository;
import com.poll.poll.repository.PollRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {


    //constructor dep inj
    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;

    public PollService(PollRepository pollRepository, PollOptionRepository pollOptionRepository) {
        this.pollRepository = pollRepository;
        this.pollOptionRepository = pollOptionRepository;
    }


    @Transactional // if any calls fail, rollback
    public PollResponse createPoll(PollRequest pollRequest) {

        //validation
        if(pollRequest.options().size() < 2
                || pollRequest.options().size() > 10
                || pollRequest.question().isBlank()
        ) {
            throw new RuntimeException("");
        }

        //create the poll first, then add the poll to the poll options
        Poll newPoll = new Poll();
        newPoll.setQuestion(pollRequest.question());

        Poll createdPoll = pollRepository.save(newPoll);

        List<PollOption> optionsToSave = pollRequest.options().stream().map(option ->  {
            PollOption pollOption = new PollOption();
            pollOption.setPoll(createdPoll);
            pollOption.setOptionText(option);
            pollOption.setVoteCount(0);
            return pollOption;

        }).toList();

        pollOptionRepository.saveAll(optionsToSave);

        List<PollOptionResponse> pollOptionsDto = optionsToSave.stream()
                .map(option -> new PollOptionResponse(
                        option.getId(),
                        option.getOptionText(),
                        option.getVoteCount()
                ))
                .toList();

        return new PollResponse(createdPoll.getId(), createdPoll.getQuestion(), pollOptionsDto);
    }
}
