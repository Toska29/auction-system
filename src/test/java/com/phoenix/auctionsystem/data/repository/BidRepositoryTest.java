package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Authority;
import com.phoenix.auctionsystem.data.model.Bid;
import com.phoenix.auctionsystem.data.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class BidRepositoryTest {

    @Autowired
    private BidRepository bidRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
        bidRepository.deleteAll();
    }

    @Test
    @Transactional
    void findBidByItemTest() {
        //given
        AppUser seller = new AppUser();
        seller.setAddress("sabo");
        seller.setFirstName("Ahmad");
        seller.setLastName("Ajala");
        seller.setEmail("ahmad@gmail.com");

        AppUser savedSeller = appUserRepository.save(seller);

        Item item = new Item();
        item.setAppUser(savedSeller);
        item.setName("BackPack");
        item.setPrice(200.0);
        item.setDescription("carriage for laptop");
        item.setQuantity(1);

        Item savedItem = itemRepository.save(item);

        AppUser buyer = new AppUser();
        buyer.setAddress("sabo");
        buyer.setFirstName("Ahmad");
        buyer.setLastName("Ajala");
        buyer.setEmail("ajala@gmail.com");

        AppUser savedBuyer = appUserRepository.save(buyer);

        Bid bid = new Bid();
        bid.setItem(savedItem);
        bid.setAppUser(savedBuyer);
        bid.setCurrentBidPrice(300.0);

        bidRepository.save(bid);
        //when
        Pageable page = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        List<Bid> bidByItem = bidRepository.findBidByItem(item, page).getContent();

        //then
        assertThat(bidByItem).isNotNull();
        assertThat(bidByItem.get(0).getItem()).isEqualTo(item);
        log.info("first bid -> {}", bidByItem);
    }

    @Test
    void findBidByAppUser() {
        //given
        AppUser seller = new AppUser();
        seller.setAddress("sabo");
        seller.setFirstName("Ahmad");
        seller.setLastName("Ajala");
        seller.setEmail("ahmad@gmail.com");

        AppUser savedSeller = appUserRepository.save(seller);

        Item item = new Item();
        item.setAppUser(savedSeller);
        item.setName("BackPack");
        item.setPrice(200.0);
        item.setDescription("carriage for laptop");
        item.setQuantity(1);

        Item savedItem = itemRepository.save(item);

        AppUser buyer = new AppUser();
        buyer.setAddress("sabo");
        buyer.setFirstName("Ahmad");
        buyer.setLastName("Ajala");
        buyer.setEmail("ajala@gmail.com");

        AppUser savedBuyer = appUserRepository.save(buyer);

        Bid bid = new Bid();
        bid.setItem(savedItem);
        bid.setAppUser(savedBuyer);
        bid.setCurrentBidPrice(300.0);

        bidRepository.save(bid);
        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        List<Bid> bidByItem = bidRepository.findBidByAppUser(savedBuyer, pageable).getContent();

        //then
        assertThat(bidByItem).isNotNull();
        assertThat(bidByItem.get(0).getItem()).isEqualTo(item);
        log.info("first bid -> {}", bidByItem);
    }
}