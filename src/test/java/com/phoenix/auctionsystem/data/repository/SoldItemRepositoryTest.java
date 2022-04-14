package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Authority;
import com.phoenix.auctionsystem.data.model.Item;
import com.phoenix.auctionsystem.data.model.SoldItem;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@Slf4j
class SoldItemRepositoryTest {
    @Autowired
    SoldItemRepository soldItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    AppUserRepository appUserRepository;

    AppUser savedSeller;

    AppUser savedBidder;

    @BeforeEach
    void setUp() {

        AppUser seller = new AppUser("Toska", "Akin", "toska@gmail.com",
                "Sabo", "09033310100", "mypassword");

        savedSeller = appUserRepository.save(seller);

        AppUser bidder = new AppUser( "Toska", "Akin", "akin@gmail.com", "Sabo",
                "09033310101", "mypassword");

        savedBidder = appUserRepository.save(bidder);
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
        soldItemRepository.deleteAll();
    }

    @Test
    void findSoldItemByItem() {
        //given
        Item item = new Item();
        item.setAppUser(savedSeller);
        item.setDescription("backpack");
        item.setName("bag");
        item.setPrice(300.0);

        Item savedItem = itemRepository.save(item);

        SoldItem soldItem = new SoldItem();
        soldItem.setItem(savedItem);
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setSoldPrice(500.0);
        soldItem.setBuyerEmail(savedBidder.getEmail());
        soldItem.setSellerEmail(savedSeller.getEmail());

        SoldItem savedSoldItem = soldItemRepository.save(soldItem);

        //when
        SoldItem querySoldItem = soldItemRepository.findSoldItemByItem(item).orElse(null);

        //assert
        assertThat(querySoldItem).isNotNull();
        assertThat(querySoldItem.getSoldPrice()).isEqualTo(500.0);

    }

    @Test
    void findSoldItemByBuyerEmail() {
        //given
        Item item = new Item();
        item.setAppUser(savedSeller);
        item.setDescription("backpack");
        item.setName("bag");
        item.setPrice(300.0);

        Item savedItem = itemRepository.save(item);

        SoldItem soldItem = new SoldItem();
        soldItem.setItem(savedItem);
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setSoldPrice(500.0);
        soldItem.setBuyerEmail(savedBidder.getEmail());
        soldItem.setSellerEmail(savedSeller.getEmail());

        soldItemRepository.save(soldItem);

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateSold"));
        List<SoldItem> soldItemList = soldItemRepository.findSoldItemByBuyerEmail(savedBidder.getEmail(), pageable).getContent();

        //assert
        assertThat(soldItemList).isNotNull();
        assertThat(soldItemList.size()).isEqualTo(1);
        log.info("Recovered sold items by buyer -> {}", soldItemList);

    }

    @Test
    void findSoldItemBySellerEmail() {
        //given
        Item item = new Item();
        item.setAppUser(savedSeller);
        item.setDescription("backpack");
        item.setName("bag");
        item.setPrice(300.0);

        Item savedItem = itemRepository.save(item);

        SoldItem soldItem = new SoldItem();
        soldItem.setItem(savedItem);
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setSoldPrice(500.0);
        soldItem.setBuyerEmail(savedBidder.getEmail());
        soldItem.setSellerEmail(savedSeller.getEmail());

        soldItemRepository.save(soldItem);

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateSold"));
        List<SoldItem> soldItemList = soldItemRepository.findSoldItemBySellerEmail(savedSeller.getEmail(), pageable).getContent();

        //assert
        assertThat(soldItemList).isNotNull();
        assertThat(soldItemList.size()).isEqualTo(1);
        log.info("Recovered sold items by seller -> {}", soldItemList);

    }
}