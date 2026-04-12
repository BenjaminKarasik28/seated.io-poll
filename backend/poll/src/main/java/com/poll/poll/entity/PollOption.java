package com.poll.poll.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
public class PollOption {
    @Id
    private Long id;

    private String optionText;

    private int voteCount;

    @OneToMany(mappedBy = "pollOption", cascade = CascadeType.ALL)
    private List<Vote> votes;

    @ManyToOne
    private Poll poll;
}
