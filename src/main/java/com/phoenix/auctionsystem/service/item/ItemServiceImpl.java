package com.phoenix.auctionsystem.service.item;

import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Item;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.data.repository.ItemRepository;
import com.phoenix.auctionsystem.dto.request.ItemRequestDto;
import com.phoenix.auctionsystem.dto.response.ItemResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import com.phoenix.auctionsystem.service.cloud.CloudService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {

    private AppUserRepository appUserRepository;

    private ModelMapper modelMapper;

    private ItemRepository itemRepository;

    private CloudService cloudService;

    @Override
    public ItemResponseDto createItem(ItemRequestDto itemRequestDto) throws UserNotFoundException, IOException {
        AppUser appUser = appUserRepository.findById(itemRequestDto.getAppUserId()).
                orElseThrow(() -> new UserNotFoundException("User does not exist"));

        Item item = modelMapper.map(itemRequestDto, Item.class);
        item.setAppUser(appUser);
        if(itemRequestDto.getImage() != null) {
            Map<?, ?> uploadResult = cloudService.upload(itemRequestDto.getImage().getBytes(),
                    ObjectUtils.asMap("public_id", "Listing/" + itemRequestDto.getImage().getOriginalFilename(),
                            "overwrite", true));

            item.setImageUrl(uploadResult.get("url").toString());
        }

        Item savedItem = itemRepository.save(item);

        return modelMapper.map(savedItem, ItemResponseDto.class);
    }

    @Override
    public List<ItemResponseDto> getAllItems(Pageable pageable) {
        List<Item> itemList = itemRepository.findAll(pageable).getContent();
        return buildItemListResponseDto(itemList);
    }

    private List<ItemResponseDto> buildItemListResponseDto(List<Item> items) {
        List<ItemResponseDto> responseDtoList = new ArrayList<>();
        for (Item item : items) {
            responseDtoList.add(modelMapper.map(item, ItemResponseDto.class));
        }
        return responseDtoList;
    }

    @Override
    public ItemResponseDto getItemById(Long id) throws AuctionSystemException {
        Item item = itemRepository.findById(id).orElseThrow(() -> new AuctionSystemException("Item not present"));

        return modelMapper.map(item, ItemResponseDto.class);
    }

    @Override
    public List<ItemResponseDto> getItemByAppUser(Long appUserId, Pageable pageable) throws UserNotFoundException {
        AppUser appUser = appUserRepository.findById(appUserId).orElseThrow(() -> new UserNotFoundException("User does not exception"));

        List<Item> itemList = itemRepository.findAllByAppUser(appUser, pageable).getContent();

        return buildItemListResponseDto(itemList);
    }

    @Override
    public List<ItemResponseDto> getItemByName(String name, Pageable pageable) throws AuctionSystemException {
        if(!itemRepository.existsByName(name)) throw new AuctionSystemException("Product does not exist");
        List<Item> itemList = itemRepository.findItemsByName(name, pageable).getContent();

        return buildItemListResponseDto(itemList);
    }

    @Override
    public ItemResponseDto updateItemDetail(Long itemId, JsonPatch patch) throws AuctionSystemException, JsonPatchException, JsonProcessingException {
        Item targetItem = itemRepository.findById(itemId).orElseThrow(() -> new AuctionSystemException("Item of id " + itemId + "does not exist"));
        Item item = applyPatchToItem(patch, targetItem);

        Item savedItem = itemRepository.save(item);
        return modelMapper.map(savedItem, ItemResponseDto.class);
    }

    private Item applyPatchToItem(JsonPatch itemPatch, Item targetItem) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        JsonNode patch = itemPatch.apply(objectMapper.convertValue(targetItem, JsonNode.class));

        return objectMapper.treeToValue(patch, Item.class);
    }

    @Override
    public void removeItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
