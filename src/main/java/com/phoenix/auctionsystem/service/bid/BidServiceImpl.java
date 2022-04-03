package com.phoenix.auctionsystem.service.bid;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Bid;
import com.phoenix.auctionsystem.data.model.Item;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.data.repository.BidRepository;
import com.phoenix.auctionsystem.data.repository.ItemRepository;
import com.phoenix.auctionsystem.dto.request.BidRequestDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BidServiceImpl implements BidService{
    private BidRepository bidRepository;
    private AppUserRepository appUserRepository;
    private ItemRepository itemRepository;

    @Override
    public Bid createBid(BidRequestDto bidRequestDto) throws AuctionSystemException, UserNotFoundException {
        Bid newBid = new Bid();
        AppUser bidder = appUserRepository.findById(bidRequestDto.getBidderId())
                .orElseThrow(() -> new UserNotFoundException("User does not exist"));

        Item item = itemRepository.findById(bidRequestDto.getItemId())
                .orElseThrow(() -> new AuctionSystemException("Item does not exist"));

        newBid.setAppUser(bidder);
        newBid.setItem(item);
        newBid.setCurrentBidPrice(bidRequestDto.getBidPrice());

        return bidRepository.save(newBid);

    }

    @Override
    public List<Bid> findBidByItem(Long itemId, Pageable pageable) throws AuctionSystemException {
        Item queryItem = itemRepository.findById(itemId).orElseThrow(() -> new AuctionSystemException("item does not exist"));
        return bidRepository.findBidByItem(queryItem, pageable).getContent();
    }

    @Override
    public List<Bid> findBidByBidder(Long bidderId, Pageable pageable) throws UserNotFoundException {
        AppUser queryBidder = appUserRepository.findById(bidderId).orElseThrow(() -> new UserNotFoundException("User does not exist"));
        return bidRepository.findBidByAppUser(queryBidder, pageable).getContent();

    }
}
