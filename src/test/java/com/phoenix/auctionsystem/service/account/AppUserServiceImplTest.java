package com.phoenix.auctionsystem.service.account;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.model.Authority;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import com.phoenix.auctionsystem.dto.request.AppUserRequestDto;
import com.phoenix.auctionsystem.dto.response.AppUserResponseDto;
import com.phoenix.auctionsystem.exception.AuctionSystemException;
import com.phoenix.auctionsystem.exception.UserDetailExistException;
import com.phoenix.auctionsystem.exception.UserNotFoundException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    private ModelMapper modelMapper;

    @Mock
    private Page<AppUser> appUserPageable;

    private AppUserService appUserService;

    @BeforeEach
    void startWithThis() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setSkipNullEnabled(true);

        appUserService = new AppUserServiceImpl(appUserRepository, modelMapper);
    }

    @Test
    @DisplayName("Test that AppUser can be registered")
    void createAccountTest() throws UserDetailExistException {
        //given
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setAddress("sabo yaba");
        requestDto.setFirstName("kim");
        requestDto.setLastName("Jane");
        requestDto.setPassword("kimjane");
        requestDto.setPhoneNumber("09036511907");
        requestDto.setEmail("kim@gmail.com");

        //when
        AppUserResponseDto responseDto = appUserService.createAccount(requestDto);

        ArgumentCaptor<AppUser> argumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        //then
        verify(appUserRepository).save(argumentCaptor.capture());

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getUserFirstName()).isEqualTo("kim");
        log.info("app user -> {}", argumentCaptor.getValue());
        log.info("saved app user -> {}", responseDto);

    }

    @Test
    @DisplayName("Throw exception Test")
    void willThrowExceptionWhenEmailExistTest() throws UserDetailExistException {
        //given
        AppUserRequestDto requestDto = new AppUserRequestDto();
        requestDto.setAddress("sabo yaba");
        requestDto.setFirstName("kim");
        requestDto.setLastName("Jane");
        requestDto.setPassword("kimjane");
        requestDto.setPhoneNumber("09036511907");
        requestDto.setEmail("kim@gmail.com");

        given(appUserRepository.existsByEmail(anyString())).willReturn(true);

        //when
        //then
        assertThatThrownBy(() -> appUserService.createAccount(requestDto))
                .isInstanceOf(UserDetailExistException.class)
                .hasMessage("This " + requestDto.getEmail() + " is taken");

        verify(appUserRepository, never()).save(any());
    }

    @Test
    @DisplayName("Test that all app can be gotten")
    void getAllAppUsersTest() {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "email", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        AppUser secondUser = new AppUser(1L, "Ahmad", "Ajala", "email", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());
        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dateCreated").descending());
        when(appUserRepository.findAll(pageable)).thenReturn(appUserPageable);

        List<AppUserResponseDto> allAppUsers = appUserService.getAllAppUsers(pageable);

        //then
        verify(appUserRepository, times(1)).findAll(pageable);
        assertThat(allAppUsers).isNotNull();

    }


    @Test
    void findAppUserByIdTest() throws UserNotFoundException {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(firstUser));
        //when
        AppUserResponseDto appUserById = appUserService.findAppUserById(1L);

        //then
        verify(appUserRepository, times(1)).findById(anyLong());
        assertThat(appUserById).isNotNull();
        assertThat(appUserById.getUserFirstName()).isEqualTo("Ahmad");
    }

    @Test
    void findAppUserByEmailTest() throws UserNotFoundException {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        when(appUserRepository.findByEmail(anyString())).thenReturn(Optional.of(firstUser));
        //when
        AppUserResponseDto appUserByEmail = appUserService.findAppUserByEmail("ahmad@gmail.com");

        //then
        verify(appUserRepository, times(1)).findByEmail(anyString());
        assertThat(appUserByEmail).isNotNull();
        assertThat(appUserByEmail.getUserFirstName()).isEqualTo("Ahmad");
    }

    @Test
    void updateAppUserDetail() throws IOException, UserNotFoundException, JsonPatchException, AuctionSystemException {
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());

        AppUser updatedUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "mypassword", LocalDateTime.now());


        String patchOp = "[\n" +
                "  {\"op\":\"replace\", \"path\":\"/password\", \"value\": \"mypassword\"}\n" +
                "\n" +
                "]";

        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(patchOp);
        JsonPatch patch = JsonPatch.fromJson(jsonNode);

        when(appUserRepository.findById(1L)).thenReturn(Optional.of(firstUser));
        when(appUserRepository.save(any(AppUser.class))).thenReturn(updatedUser);
        when(appUserRepository.existsByEmail(anyString())).thenReturn(false);
        AppUserResponseDto responseDto = appUserService.updateAppUserDetail(1L, patch);

        //then
        verify(appUserRepository, times(1)).findById(firstUser.getId());
        verify(appUserRepository, times(1)).save(any(AppUser.class));

        assertThat(responseDto).isNotNull();
        assertThat(responseDto.getUserAddress()).isEqualTo("sabo");

    }

    @SneakyThrows(UserNotFoundException.class)
    @Test
    @DisplayName("Test that user can be removed")
    void userCanBeRemovedTest(){
        //given
        AppUser firstUser = new AppUser(1L, "Ahmad", "Ajala", "ahmad@gmail.com", "sabo",
                List.of(Authority.SELLER, Authority.SELLER), "08122210111", "", LocalDateTime.now());
        when(appUserRepository.existsById(1L)).thenReturn(true);
        //when
        appUserService.removeAppUser(1L);
        //then
        verify(appUserRepository).deleteById(anyLong());
    }
}