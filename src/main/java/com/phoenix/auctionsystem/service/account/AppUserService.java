package com.phoenix.auctionsystem.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.auctionsystem.dto.request.AppUserRequestDto;
import com.phoenix.auctionsystem.dto.response.AppUserResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserDetailExistException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppUserService {
    AppUserResponseDto createAccount(AppUserRequestDto appUserRequestDto) throws UserDetailExistException;

    List<AppUserResponseDto> getAllAppUsers(Pageable pageable);

    AppUserResponseDto findAppUserById(Long appUserId) throws UserNotFoundException;

    AppUserResponseDto findAppUserByEmail(String email) throws UserNotFoundException;

    AppUserResponseDto updateAppUserDetail(Long appUserId, JsonPatch appUserPatch) throws UserNotFoundException, JsonPatchException, JsonProcessingException, AuctionSystemException;

    void removeAppUser(Long appUserId) throws UserNotFoundException;

}
