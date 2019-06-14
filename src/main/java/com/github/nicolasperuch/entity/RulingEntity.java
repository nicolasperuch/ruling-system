package com.github.nicolasperuch.entity;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "ruling", schema = "ruling")
public class RulingEntity {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "open_for_vote")
    private boolean openForVote;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpenForVote() {
        return openForVote;
    }

    public void setOpenForVote(boolean openForVote) {
        this.openForVote = openForVote;
    }
}
