package com.phoenix.auctionsystem.data.model;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.phoenix.auctionsystem.data.model.Authority.BUYER;
import static com.phoenix.auctionsystem.data.model.Authority.SELLER;

@Entity
@Setter
@Getter
@AllArgsConstructor
@ToString
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;
    private String address;

    @ElementCollection
    private List<Authority> roles;

    @Column(unique = true)
    private String phoneNumber;

    private String password;

    @CreationTimestamp
    private LocalDateTime dateRegistered;

    public AppUser(){
        roles = new ArrayList<>(Arrays.asList(BUYER, SELLER));
    }
}
