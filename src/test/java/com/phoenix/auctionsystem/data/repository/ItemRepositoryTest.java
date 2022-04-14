package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Item;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Slf4j
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    AppUser appUser;

    Item item;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();

        appUser.setEmail("toska@gmail.com");
        appUser.setLastName("Akin");
        appUser.setFirstName("Toska");
        appUser.setAddress("sabo");
        item = new Item("bag", "backpack", 45.0,
                1, appUser);

        itemRepository.save(item);
    }

    @AfterEach
    void tearDown() {
        itemRepository.deleteAll();
    }

    @Test
    @DisplayName("Find Items By AppUser Test")
    void testThatItemsCanBeFoundByAppUser() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());

        //when
        Page<Item> items = itemRepository.findAllByAppUser(appUser, pageable);

        //then
        assertThat(items).isNotNull();
        assertThat(items.getTotalElements()).isEqualTo(1);

        log.info("Retrieved items -> {}", items.getContent());
    }

    @Test
    void findItemsByName() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());

        //when
        Page<Item> items = itemRepository.findItemsByName("bag", pageable);

        //then
        assertThat(items).isNotNull();
        assertThat(items.getTotalElements()).isEqualTo(1);

        log.info("Retrieved items -> {}", items.getContent());
    }
}