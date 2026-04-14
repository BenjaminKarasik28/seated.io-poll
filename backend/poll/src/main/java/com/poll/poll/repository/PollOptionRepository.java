package com.poll.poll.repository;

import com.poll.poll.entity.PollOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PollOptionRepository extends JpaRepository<PollOption, Long> {

    List<PollOption> findAllByPollId(Long id);

    @Modifying
    @Query("""
    UPDATE PollOption pollOption
    SET pollOption.voteCount = pollOption.voteCount + 1
    WHERE pollOption.id = :optionId
""")
    void incrementVoteCount(@Param("optionId") Long optionId);
}
