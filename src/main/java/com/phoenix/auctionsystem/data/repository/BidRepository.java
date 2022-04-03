package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Bid;
import com.phoenix.auctionsystem.data.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BidRepository extends JpaRepository<Bid, Long> {
    Page<Bid> findBidByItem(Item item,  Pageable pageable);
    Page<Bid> findBidByAppUser(AppUser appUser, Pageable pageable);
}
