package com.phoenix.auctionsystem.data.model;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@NoArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String event;
    @ManyToMany
    private List<AppUser> appUsers;
}
