package com.phoenix.auctionsystem.service.bid;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Bid;
import com.phoenix.auctionsystem.data.model.Item;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.data.repository.BidRepository;
import com.phoenix.auctionsystem.data.repository.ItemRepository;
import com.phoenix.auctionsystem.dto.request.BidRequestDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Slf4j
class BidServiceImplTest {

    @Mock
    BidRepository bidRepository;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    Page<Bid> bidPage;

    BidService bidService;

    @BeforeEach
    void setUp() {
        bidService = new BidServiceImpl(bidRepository, appUserRepository, itemRepository);
    }

    @AfterEach
    void tearDown() {

    }

    @SneakyThrows
    @Test
    void createBidTest() {
        //given
        BidRequestDto bidRequestDto = new BidRequestDto();
        bidRequestDto.setBidPrice(100.0);
        bidRequestDto.setBidderId(1L);
        bidRequestDto.setItemId(1L);

        AppUser lister = new AppUser();
        lister.setId(1L);
        lister.setEmail("samson@gmail.com");
        lister.setFirstName("Ahmad");
        lister.setLastName("Samson");
        lister.setAddress("Link");

        AppUser bidder = new AppUser();
        bidder.setId(1L);
        bidder.setEmail("ahmad@gmail.com");
        bidder.setFirstName("Ahmad");
        bidder.setLastName("Samson");
        bidder.setAddress("Link");

        Item item = new Item();
        item.setId(1L);
        item.setPrice(200.0);
        item.setAppUser(lister);
        item.setQuantity(1);
        item.setName("backpack");
        item.setDescription("school bag");
        item.setDateCreated(LocalDateTime.now());

        when(appUserRepository.findById(anyLong())).thenReturn(java.util.Optional.of(bidder));
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        //when
        bidService.createBid(bidRequestDto);
        //then
        verify(appUserRepository).findById(anyLong());
        verify(itemRepository).findById(anyLong());
        verify(bidRepository).save(any(Bid.class));
    }

    @SneakyThrows
    @Test
    void findBidByItemTest() {
        //given

        Item item = new Item();
        item.setId(1L);
        item.setPrice(200.0);
        item.setQuantity(1);
        item.setName("backpack");
        item.setDescription("school bag");
        item.setDateCreated(LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(bidRepository.findBidByItem(item, pageable)).thenReturn(bidPage);

        //when
        List<Bid> bidByItem = bidService.findBidByItem(1L, pageable);
        //then
        verify(bidRepository).findBidByItem(item, pageable);

    }

    @SneakyThrows
    @Test
    void findBidByBidderTest() {
        //given
        AppUser bidder = new AppUser();
        bidder.setId(1L);
        bidder.setEmail("ahmad@gmail.com");
        bidder.setFirstName("Ahmad");
        bidder.setLastName("Samson");
        bidder.setAddress("Link");

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(bidder));
        when(bidRepository.findBidByAppUser(bidder, pageable)).thenReturn(bidPage);

        //when
        List<Bid> bidByItem = bidService.findBidByBidder(1L, pageable);

        //then
        verify(bidRepository).findBidByAppUser(bidder, pageable);
    }
}