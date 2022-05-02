package com.phoenix.auctionsystem.service.soldItem;

import com.phoenix.auctionsystem.data.model.*;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.data.repository.BidRepository;
import com.phoenix.auctionsystem.data.repository.ItemRepository;
import com.phoenix.auctionsystem.data.repository.SoldItemRepository;
import com.phoenix.auctionsystem.dto.response.SoldItemResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class SoldItemServiceImplTest {

    SoldItemService soldItemService;

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    SoldItemRepository soldItemRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    BidRepository bidRepository;

    @Mock
    Page<SoldItem> soldItemPage;

    ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        soldItemService = new SoldItemServiceImpl(appUserRepository, bidRepository, soldItemRepository, itemRepository, modelMapper);
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void createSoldItemTest() {
        //given
        AppUser seller = new AppUser(1L, "Toska", "Akin", "toska@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310101", "mypassword", LocalDateTime.now());

        AppUser buyer = new AppUser(2L, "Toska", "Akin", "akin@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310102", "mypassword", LocalDateTime.now());

        Item item = new Item(1L, "cellphone", "samsung s5", 200.0, 1,
                null, seller, LocalDateTime.now());

        Bid bid = new Bid();
        bid.setCurrentBidPrice(400.0);
        bid.setItem(item);
        bid.setAppUser(buyer);
        bid.setDateCreated(LocalDateTime.now());

        SoldItem soldItem = new SoldItem();

        when(bidRepository.findById(anyLong())).thenReturn(Optional.of(bid));
        ArgumentCaptor<SoldItem> argumentCaptor = ArgumentCaptor.forClass(SoldItem.class);
        when(soldItemRepository.save(argumentCaptor.capture())).thenReturn(soldItem);
        //when
        SoldItemResponseDto soldItemResponseDto = soldItemService.createSoldItem(1L);

        //then
        verify(bidRepository).findById(anyLong());
        verify(soldItemRepository).save(any(SoldItem.class));
    }

    @Test
    void throwExceptionWhen_bidIdDoesNotExistTest() throws AuctionSystemException {
        //given
        AppUser seller = new AppUser(1L, "Toska", "Akin", "toska@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310101", "mypassword", LocalDateTime.now());

        AppUser buyer = new AppUser(2L, "Toska", "Akin", "akin@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310102", "mypassword", LocalDateTime.now());

        Item item = new Item(1L, "cellphone", "samsung s5", 200.0, 1,
                null, seller, LocalDateTime.now());

        Bid bid = new Bid();
        bid.setCurrentBidPrice(400.0);
        bid.setItem(item);
        bid.setAppUser(buyer);
        bid.setDateCreated(LocalDateTime.now());

        //when
        //then
        assertThatThrownBy(() -> soldItemService.createSoldItem(1L))
                .isInstanceOf(AuctionSystemException.class)
                .hasMessage("Bid with id: " + 1L + " does not exist");
        verify(bidRepository, times(1)).findById(anyLong());
        verify(soldItemRepository, never()).save(any(SoldItem.class));
    }

    @SneakyThrows
    @Test
    void findSoldItemById() {
        //given
        AppUser seller = new AppUser(1L, "Toska", "Akin", "toska@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310101", "mypassword", LocalDateTime.now());

        AppUser buyer = new AppUser(2L, "Toska", "Akin", "akin@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310102", "mypassword", LocalDateTime.now());

        Item item = new Item(1L, "cellphone", "samsung s5", 200.0, 1,
                null, seller, LocalDateTime.now());

        Bid bid = new Bid();
        bid.setCurrentBidPrice(400.0);
        bid.setItem(item);
        bid.setAppUser(buyer);
        bid.setDateCreated(LocalDateTime.now());

        SoldItem soldItem = new SoldItem();
        soldItem.setBuyerEmail(buyer.getEmail());
        soldItem.setItem(item);
        soldItem.setSoldPrice(bid.getCurrentBidPrice());
        soldItem.setSellerEmail(seller.getEmail());
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setId(1L);

        when(soldItemRepository.findById(anyLong())).thenReturn(Optional.of(soldItem));
        //when
        SoldItemResponseDto soldItemById = soldItemService.findSoldItemById(1L);
        //then
        verify(soldItemRepository, times(1)).findById(1L);

    }

    @SneakyThrows
    @Test
    void findSoldItemByItem() {
        //given
        AppUser seller = new AppUser(1L, "Toska", "Akin", "toska@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310101", "mypassword", LocalDateTime.now());

        AppUser buyer = new AppUser(2L, "Toska", "Akin", "akin@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310102", "mypassword", LocalDateTime.now());

        Item item = new Item(1L, "cellphone", "samsung s5", 200.0, 1,
                null, seller, LocalDateTime.now());

        Bid bid = new Bid();
        bid.setCurrentBidPrice(400.0);
        bid.setItem(item);
        bid.setAppUser(buyer);
        bid.setDateCreated(LocalDateTime.now());

        SoldItem soldItem = new SoldItem();
        soldItem.setBuyerEmail(buyer.getEmail());
        soldItem.setItem(item);
        soldItem.setSoldPrice(bid.getCurrentBidPrice());
        soldItem.setSellerEmail(seller.getEmail());
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setId(1L);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(soldItemRepository.findSoldItemByItem(any(Item.class))).thenReturn(Optional.of(soldItem));
        //when
        soldItemService.findSoldItemByItem(1L);
        //then
        verify(itemRepository).findById(anyLong());
        verify(soldItemRepository).findSoldItemByItem(any(Item.class));
    }

    @Test
    void findSoldItemBySellerEmail() throws UserNotFoundException {
        //given
        AppUser seller = new AppUser(1L, "Toska", "Akin", "toska@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310101", "mypassword", LocalDateTime.now());

        AppUser buyer = new AppUser(2L, "Toska", "Akin", "akin@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310102", "mypassword", LocalDateTime.now());

        Item item = new Item(1L, "cellphone", "samsung s5", 200.0, 1,
                null, seller, LocalDateTime.now());

        Bid bid = new Bid();
        bid.setCurrentBidPrice(400.0);
        bid.setItem(item);
        bid.setAppUser(buyer);
        bid.setDateCreated(LocalDateTime.now());

        SoldItem soldItem = new SoldItem();
        soldItem.setBuyerEmail(buyer.getEmail());
        soldItem.setItem(item);
        soldItem.setSoldPrice(bid.getCurrentBidPrice());
        soldItem.setSellerEmail(seller.getEmail());
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setId(1L);

        when(appUserRepository.existsByEmail(anyString())).thenReturn(true);
        when(soldItemRepository.findSoldItemBySellerEmail(anyString(), any(Pageable.class)))
                .thenReturn(soldItemPage);

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateSold").descending());
        List<SoldItemResponseDto> soldItems = soldItemService.findSoldItemBySellerEmail("toska@gmail.com", pageable);
        //then
        verify(appUserRepository).existsByEmail(anyString());
        verify(soldItemRepository).findSoldItemBySellerEmail(anyString(), any(Pageable.class));
    }

    @SneakyThrows
    @Test
    void findSoldItemByBuyerEmail() {
        //given
        AppUser seller = new AppUser(1L, "Toska", "Akin", "toska@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310101", "mypassword", LocalDateTime.now());

        AppUser buyer = new AppUser(2L, "Toska", "Akin", "akin@gmail.com", "palace",
                List.of(Authority.SELLER, Authority.BUYER), "08133310102", "mypassword", LocalDateTime.now());

        Item item = new Item(1L, "cellphone", "samsung s5", 200.0, 1,
                null, seller, LocalDateTime.now());

        Bid bid = new Bid();
        bid.setCurrentBidPrice(400.0);
        bid.setItem(item);
        bid.setAppUser(buyer);
        bid.setDateCreated(LocalDateTime.now());

        SoldItem soldItem = new SoldItem();
        soldItem.setBuyerEmail(buyer.getEmail());
        soldItem.setItem(item);
        soldItem.setSoldPrice(bid.getCurrentBidPrice());
        soldItem.setSellerEmail(seller.getEmail());
        soldItem.setDateSold(LocalDateTime.now());
        soldItem.setId(1L);

        when(appUserRepository.existsByEmail(anyString())).thenReturn(true);
        when(soldItemRepository.findSoldItemByBuyerEmail(anyString(), any(Pageable.class)))
                .thenReturn(soldItemPage);

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateSold").descending());
        soldItemService.findSoldItemByBuyerEmail("akin@gmail.com", pageable);
        //then
        verify(appUserRepository).existsByEmail(anyString());
        verify(soldItemRepository).findSoldItemByBuyerEmail(anyString(), any(Pageable.class));
    }
}