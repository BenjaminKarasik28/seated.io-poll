package com.poll.poll.service;


import com.poll.poll.dto.poll.PollRequest;
import com.poll.poll.dto.poll.PollResponse;
import com.poll.poll.dto.polloption.PollOptionResponse;
import com.poll.poll.entity.Poll;
import com.poll.poll.entity.PollOption;
import com.poll.poll.repository.PollOptionRepository;
import com.poll.poll.repository.PollRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        //create the poll first, then add the poll to the poll options
        Poll newPoll = new Poll();
        newPoll.setQuestion(pollRequest.question());

        Poll createdPoll = pollRepository.save(newPoll);

        List<PollOption> optionsToSave = pollRequest
                .options()
                .stream()
                .map(option ->  {
            PollOption pollOption = new PollOption();
            pollOption.setPoll(createdPoll);
            pollOption.setOptionText(option);
            pollOption.setVoteCount(0);
            return pollOption;

        }).toList();

        pollOptionRepository.saveAll(optionsToSave);

        List<PollOptionResponse> pollOptionsDto = optionsToSave
                .stream()
                .map(option -> new PollOptionResponse(
                        option.getId(),
                        option.getOptionText(),
                        option.getVoteCount()
                ))
                .toList();

        return new PollResponse(createdPoll.getId(), createdPoll.getQuestion(), pollOptionsDto);
    }

    @Transactional
    public PollResponse getPollById(Long id) {

        Poll fetchedPoll = pollRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Poll not found"
        ));

        List<PollOptionResponse> pollOptionsDto = pollOptionRepository
                .findAllByPollId(id)
                .stream()
                .map(pollOptionFromDb -> new PollOptionResponse(
                        pollOptionFromDb.getId(),
                        pollOptionFromDb.getOptionText(),
                        pollOptionFromDb.getVoteCount()
                ))
                .toList();


        return new PollResponse(fetchedPoll.getId(), fetchedPoll.getQuestion(), pollOptionsDto);
    }

    @Transactional
    public PollOptionResponse vote(Long id) {

        pollOptionRepository.incrementVoteCount(id);
        pollOptionRepository.flush();
        PollOption pollOption = pollOptionRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Poll option not found"
                ));

        return new PollOptionResponse(pollOption.getId(), pollOption.getOptionText(), pollOption.getVoteCount());

    }
}
