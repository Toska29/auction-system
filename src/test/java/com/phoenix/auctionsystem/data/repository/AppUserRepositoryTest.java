package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Authority;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Slf4j
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setFirstName("Tosin");
        appUser.setEmail("toska@gmail.com");
        appUser.setRoles(new ArrayList<>(Arrays.asList(Authority.BUYER, Authority.SELLER)));
        appUser.setLastName("Akin");
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
    }

    @Test
    @DisplayName("Test that App User can be saved")
    void testThatAppUserCanBeAddedToDatabase_whenCreated() {
        //given
        ;
        //when
        AppUser savedUser = appUserRepository.save(appUser);

        //then
        assertThat(appUserRepository.findAll().size()).isEqualTo(1);
        log.info("Saved user -> {}", savedUser);

    }

    @Test
    @DisplayName("Find User by email")
    void testThatAppUserCanBeFoundByEmail() {
        //given
        AppUser savedUser = appUserRepository.save(appUser);

        //when
        Optional<AppUser> queryUser = appUserRepository.findByEmail(appUser.getEmail());
        //then
        assertThat(queryUser.get()).isNotNull();
        assertThat(queryUser.get().getFirstName()).isEqualTo("Tosin");

        log.info("query user -> {}", queryUser.get());
    }

    @Test
    @DisplayName("Test when user is search by exist email")
    void whenAppUserIsDetermined_byEmailExistsTest(){
        //given
        AppUser savedUser = appUserRepository.save(appUser);

        //when
        boolean queryUser = appUserRepository.existsByEmail(appUser.getEmail());
        //then
        assertThat(queryUser).isTrue();

    }
}