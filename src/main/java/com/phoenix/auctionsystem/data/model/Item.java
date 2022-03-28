package com.phoenix.auctionsystem.data.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private Double price;
    private Integer quantity;
    private String imageUrl;
    @ManyToOne
    private AppUser appUser;
}
