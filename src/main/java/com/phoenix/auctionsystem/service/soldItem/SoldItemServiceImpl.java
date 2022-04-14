package com.phoenix.auctionsystem.service.soldItem;

import com.phoenix.auctionsystem.data.model.Bid;
import com.phoenix.auctionsystem.data.model.SoldItem;
import com.phoenix.auctionsystem.data.repository.BidRepository;
import com.phoenix.auctionsystem.data.repository.SoldItemRepository;
import com.phoenix.auctionsystem.dto.response.SoldItemResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SoldItemServiceImpl implements SoldItemService{

    private final BidRepository bidRepository;
    private final SoldItemRepository soldItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public SoldItemResponseDto createSoldItem(Long bidId) throws AuctionSystemException {
        Bid bid = bidRepository.findById(bidId).orElseThrow(() -> new AuctionSystemException("Bid with id: " + bidId + " does not exist"));
        SoldItem soldItem = new SoldItem();
        SoldItem soldItemMap = buildSoldItem(bid, soldItem);

        SoldItem savedSoldItem = soldItemRepository.save(soldItemMap);

        return modelMapper.map(savedSoldItem, SoldItemResponseDto.class);

    }

    private SoldItem buildSoldItem(Bid bid, SoldItem soldItem) {
        soldItem.setItem(bid.getItem());
        soldItem.setSoldPrice(bid.getCurrentBidPrice());
        soldItem.setSellerEmail(bid.getItem().getAppUser().getEmail());
        soldItem.setBuyerEmail(bid.getAppUser().getEmail());
        return soldItem;
    }

    @Override
    public SoldItemResponseDto findSoldItemById(Long soldItemId) {
        return null;
    }

    @Override
    public SoldItemResponseDto findSoldItemByItem(Long itemId) {
        return null;
    }

    @Override
    public List<SoldItemResponseDto> findSoldItemBySellerEmail(String sellerEmail, Pageable pageable) {
        return null;
    }

    @Override
    public List<SoldItemResponseDto> findSoldItemByBuyerEmail(String buyerEmail, Pageable pageable) {
        return null;
    }
}
