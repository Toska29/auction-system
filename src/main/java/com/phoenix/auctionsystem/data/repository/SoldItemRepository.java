package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.Item;
import com.phoenix.auctionsystem.data.model.SoldItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SoldItemRepository extends JpaRepository<SoldItem, Long> {
    Optional<SoldItem> findSoldItemByItem(Item item);
    Page<SoldItem> findSoldItemByBuyerEmail(String buyerEmail, Pageable pageable);
    Page<SoldItem> findSoldItemBySellerEmail(String sellerEmail, Pageable pageable);
}
