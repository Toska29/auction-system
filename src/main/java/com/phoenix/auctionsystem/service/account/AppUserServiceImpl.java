package com.phoenix.auctionsystem.service.account;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.dto.request.AppUserRequestDto;
import com.phoenix.auctionsystem.dto.response.AppUserResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserDetailExistException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;

    private final ModelMapper modelMapper;

    @Override
    public AppUserResponseDto createAccount(AppUserRequestDto appUserRequestDto) throws UserDetailExistException {
        boolean queryUser = appUserRepository.existsByEmail(appUserRequestDto.getEmail());
        if (queryUser) throw new UserDetailExistException("This " + appUserRequestDto.getEmail() + " is taken");

        AppUser appUser = modelMapper.map(appUserRequestDto, AppUser.class);

        appUserRepository.save(appUser);

        return modelMapper.map(appUser, AppUserResponseDto.class);
    }

    @Override
    public List<AppUserResponseDto> getAllAppUsers(Pageable pageable) {
        List<AppUser> allUsers = appUserRepository.findAll(pageable).getContent();

        return buildAppUserResponseDto(allUsers);
    }

    private List<AppUserResponseDto> buildAppUserResponseDto(List<AppUser> allUsers) {
        List<AppUserResponseDto> responseDtoList = new ArrayList<>();
        for (AppUser user : allUsers) {
            responseDtoList.add(modelMapper.map(user, AppUserResponseDto.class));
        }
        return responseDtoList;
    }

    @Override
    public AppUserResponseDto findAppUserById(Long appUserId) throws UserNotFoundException {
        Optional<AppUser> queryUser = appUserRepository.findById(appUserId);
        if (queryUser.isEmpty()) throw new UserNotFoundException("User with id " + appUserId + " does not exist");

        return modelMapper.map(queryUser.get(), AppUserResponseDto.class);
    }

    @Override
    public AppUserResponseDto findAppUserByEmail(String email) throws UserNotFoundException {
        Optional<AppUser> queryUser = appUserRepository.findByEmail(email);
        if (queryUser.isEmpty()) throw new UserNotFoundException("User with email: " + email + " does not exist");

        return modelMapper.map(queryUser.get(), AppUserResponseDto.class);
    }

    @Override
    public AppUserResponseDto updateAppUserDetail(Long appUserId, JsonPatch appUserPatch)
            throws UserNotFoundException, AuctionSystemException {
        Optional<AppUser> queryUSer = appUserRepository.findById(appUserId);
        if (queryUSer.isEmpty()) throw new UserNotFoundException("User does not exit");

        AppUser targetUser = queryUSer.get();
        try {
            AppUser appUser = applyPatchToUser(appUserPatch, targetUser);
            if(appUserRepository.existsByEmail(appUser.getEmail())){
                throw new UserDetailExistException("User with " + appUser.getEmail() + " already exist");
            }
            AppUser updatedUser = appUserRepository.save(appUser);
            System.out.println(updatedUser);
            return modelMapper.map(updatedUser, AppUserResponseDto.class);
        } catch (JsonPatchException | JsonProcessingException | UserDetailExistException e) {
            throw new AuctionSystemException("update fail");
        }

    }

    @Override
    public void removeAppUser(Long appUserId) throws UserNotFoundException {
        if(!appUserRepository.existsById(appUserId)) throw new UserNotFoundException("User does not exist");
            appUserRepository.deleteById(appUserId);
    }

    private AppUser applyPatchToUser(JsonPatch appUserPatch, AppUser targetUser) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        JsonNode patch = appUserPatch.apply(objectMapper.convertValue(targetUser, JsonNode.class));

        return objectMapper.treeToValue(patch, AppUser.class);
    }

}
