package com.phoenix.auctionsystem.service.bid;

import com.phoenix.auctionsystem.data.model.Bid;
import com.phoenix.auctionsystem.dto.request.BidRequestDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserDetailExistException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BidService {
    Bid createBid(BidRequestDto bidRequestDto) throws UserDetailExistException, AuctionSystemException, UserNotFoundException;
    List<Bid> findBidByItem(Long itemId, Pageable pageable) throws AuctionSystemException;
    List<Bid> findBidByBidder(Long bidderId, Pageable pageable) throws UserNotFoundException;
}
