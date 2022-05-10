package com.phoenix.auctionsystem.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.auctionsystem.dto.request.AppUserRequestDto;
import com.phoenix.auctionsystem.dto.response.AppUserResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserDetailExistException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import com.phoenix.auctionsystem.service.account.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    @Autowired
    private AppUserService appUserService;

    @PostMapping("")
    public ResponseEntity<?> createUser(@RequestBody AppUserRequestDto appUserRequestDto){
        try{
            AppUserResponseDto account = appUserService.createAccount(appUserRequestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(account);
        } catch (UserDetailExistException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("{/appUserId}")
    public ResponseEntity<?> getAppUser(@PathVariable Long appUserId){
        try {
            AppUserResponseDto appUser = appUserService.findAppUserById(appUserId);
            return ResponseEntity.accepted().body(appUser);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAppUserByEmail(@RequestParam(name = "email") String email){
        try {
            AppUserResponseDto appUserByEmail = appUserService.findAppUserByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(appUserByEmail);
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAppUsers(@RequestParam(name = "page") int page,
                                            @RequestParam(name = "pageSize") int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("dateCreated").descending());
        List<AppUserResponseDto> allAppUsers = appUserService.getAllAppUsers(pageable);
        return ResponseEntity.ok(allAppUsers);
    }

    @PatchMapping("{/appUserId}")
    public ResponseEntity<?> updateAppUserDetail(@PathVariable Long appUserId, @RequestBody JsonPatch patch){
        try {
            AppUserResponseDto updateAppUser = appUserService.updateAppUserDetail(appUserId, patch);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(updateAppUser);
        } catch (UserNotFoundException | JsonProcessingException | AuctionSystemException | JsonPatchException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{/appUserId}")
    public ResponseEntity<?> removeAppUser(@PathVariable Long appUserId){
        try {
            appUserService.removeAppUser(appUserId);
            return ResponseEntity.ok().body("Removed successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
