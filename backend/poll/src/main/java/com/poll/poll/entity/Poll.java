package com.poll.poll.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private Date endsAt;

    private String status; //enabled, disabled, expired


//    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL)
//    private List<PollOption> options;
}
