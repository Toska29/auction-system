package com.phoenix.auctionsystem.service.item;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Authority;
import com.phoenix.auctionsystem.data.model.Item;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.data.repository.ItemRepository;
import com.phoenix.auctionsystem.dto.request.ItemRequestDto;
import com.phoenix.auctionsystem.dto.response.AppUserResponseDto;
import com.phoenix.auctionsystem.dto.response.ItemResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.service.cloud.CloudService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.swing.tree.TreeNode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ItemServiceImplTest {

    @Mock
    AppUserRepository appUserRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    Page<Item> itemPage;

    ModelMapper modelMapper;

    ItemService itemService;

    @Mock
    CloudService cloudService;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);
        itemService = new ItemServiceImpl(appUserRepository, modelMapper, itemRepository, cloudService);
    }

    @AfterEach
    void tearDown() {
        appUserRepository.deleteAll();
        itemRepository.deleteAll();
    }

    @SneakyThrows
    @Test
    @DisplayName("Create Item test")
    void createItemTest() {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        ItemRequestDto itemRequestDto = new ItemRequestDto();
        itemRequestDto.setAppUserId(1L);
        itemRequestDto.setName("samsung");
        itemRequestDto.setDescription("samsung note 3");

        Item item = new Item();
        item.setAppUser(firstUser);
        item.setName("bag");
        item.setDescription("samsung note 3");

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(firstUser));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        //when
        ItemResponseDto responseDto = itemService.createItem(itemRequestDto);

        //then
        verify(appUserRepository).findById(1L);
        verify(itemRepository).save(any(Item.class));

        assertThat(responseDto.getAppUserId()).isEqualTo(1L);
        assertThat(responseDto.getName()).isEqualTo("bag");

        log.info("ResponseDto -> {}", responseDto);
    }

    @Test
    @DisplayName("Test that all Items can be retrieved")
    void getAllItemsTest() {

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        when(itemRepository.findAll(pageable)).thenReturn(itemPage);

        List<ItemResponseDto> allItems = itemService.getAllItems(pageable);

        //then
        verify(itemRepository, times(1)).findAll(pageable);
        assertThat(allItems).isNotNull();
    }

    @Test
    void getItemByIdTest() throws AuctionSystemException {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        Item item = new Item();
        item.setAppUser(firstUser);
        item.setName("bag");
        item.setId(1L);
        item.setDescription("samsung note 3");

        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        //when
        ItemResponseDto queryItem = itemService.getItemById(1L);

        //then
        verify(itemRepository).findById(anyLong());
    }

    @SneakyThrows
    @Test
    void getItemByAppUserTest() {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        when(appUserRepository.findById(1L)).thenReturn(Optional.of(firstUser));
        when(itemRepository.findAllByAppUser(firstUser,pageable)).thenReturn(itemPage);
        //when

        itemService.getItemByAppUser(1L, pageable);

        //then
        verify(appUserRepository).findById(anyLong());
        verify(itemRepository).findAllByAppUser(firstUser, pageable);
    }

    @Test
    void getItemByNameTest() throws AuctionSystemException {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());

        when(itemRepository.existsByName("bag")).thenReturn(true);
        when(itemRepository.findItemsByName("bag",pageable)).thenReturn(itemPage);

        //when
        itemService.getItemByName("bag", pageable);

        //then
        verify(itemRepository).findItemsByName("bag", pageable);
    }

    @SneakyThrows
    @Test
    void testThatItemDetailCanBeUpdated(){
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        Item item = new Item();
        item.setId(1L);
        item.setPrice(1000.0);
        item.setDateCreated(LocalDateTime.now());
        item.setName("backpack");
        item.setDescription("for school accessories");
        item.setAppUser(firstUser);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        //when
        ObjectMapper mapper = new ObjectMapper();
        String patchOperation = "[\n" +
                "  {\"op\":\"replace\", \"path\":\"/price\", \"value\": 100}\n" +
                "\n" +
                "]";
        JsonNode patchNode = mapper.readTree(patchOperation);
        JsonPatch patch = JsonPatch.fromJson(patchNode);

        itemService.updateItemDetail(1L, patch);

        //then

        verify(itemRepository, times(1)).findById(anyLong());
        verify(itemRepository, times(1)).save(any(Item.class));

    }

    @Test
    void removeItemTest() {
        itemService.removeItem(1L);

        verify(itemRepository).deleteById(1L);
    }
}