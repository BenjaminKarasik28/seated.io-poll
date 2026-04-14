package com.poll.poll.repository;

import com.poll.poll.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

    List<PollOption> findAllByPollId(Long id);
}
