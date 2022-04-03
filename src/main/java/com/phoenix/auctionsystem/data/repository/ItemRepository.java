package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long > {
    Page<Item> findAllByAppUser(AppUser appUser, Pageable pageable);
    Page<Item> findItemsByName(String itemName, Pageable pageable);

    boolean existsByName(String itemName);
}
