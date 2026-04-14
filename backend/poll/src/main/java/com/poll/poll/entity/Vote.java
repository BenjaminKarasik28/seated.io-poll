package com.poll.poll.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Vote {
    @Id
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "poll_id")
//    private Poll poll;

//    @ManyToOne
//    private PollOption pollOption;

}
