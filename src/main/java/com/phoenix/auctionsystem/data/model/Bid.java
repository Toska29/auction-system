package com.phoenix.auctionsystem.data.model;

import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import java.time.Duration;

@Entity
@Setter
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Duration duration;
    private Double currentBid;

    @ManyToOne
    private AppUser appUser;
    @ManyToOne
    private Item item;
}
