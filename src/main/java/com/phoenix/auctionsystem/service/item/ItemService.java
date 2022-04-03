package com.phoenix.auctionsystem.service.item;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.auctionsystem.dto.request.ItemRequestDto;
import com.phoenix.auctionsystem.dto.response.ItemResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface ItemService {

    ItemResponseDto createItem(ItemRequestDto itemRequestDto) throws UserNotFoundException, IOException;

    List<ItemResponseDto> getAllItems(Pageable pageable);

    ItemResponseDto getItemById(Long id) throws AuctionSystemException;

    List<ItemResponseDto> getItemByAppUser(Long appUserId, Pageable pageable) throws UserNotFoundException;

    List<ItemResponseDto> getItemByName(String name, Pageable pageable) throws AuctionSystemException;

    ItemResponseDto updateItemDetail(Long itemId, JsonPatch patch) throws AuctionSystemException, JsonPatchException, JsonProcessingException;

    void removeItem(Long itemId);

}
