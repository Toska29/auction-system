package com.phoenix.auctionsystem.data.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SoldItem {
    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;
    private String buyerEmail;
    private String sellerEmail;
    private Double soldPrice;

    @OneToOne
    private Item item;

    @ManyToOne
    private AppUser appUser;

    @CreationTimestamp
    private LocalDateTime dateSold;

}
