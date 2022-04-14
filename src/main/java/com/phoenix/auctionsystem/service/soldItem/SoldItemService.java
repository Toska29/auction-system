package com.phoenix.auctionsystem.service.soldItem;

import com.phoenix.auctionsystem.dto.response.SoldItemResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SoldItemService {
    SoldItemResponseDto createSoldItem(Long bidId) throws AuctionSystemException;
    SoldItemResponseDto findSoldItemById(Long soldItemId);
    SoldItemResponseDto findSoldItemByItem(Long itemId);
    List<SoldItemResponseDto> findSoldItemBySellerEmail(String sellerEmail, Pageable pageable);
    List<SoldItemResponseDto> findSoldItemByBuyerEmail(String buyerEmail, Pageable pageable);

}
