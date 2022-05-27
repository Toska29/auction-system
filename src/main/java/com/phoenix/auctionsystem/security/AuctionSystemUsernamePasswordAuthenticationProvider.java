package com.phoenix.auctionsystem.service.security;

import com.phoenix.auctionsystem.data.model.AppUser;
import com.phoenix.auctionsystem.data.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class AuctionSystemUsernamePasswordAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        Optional<AppUser> queryAppUser = appUserRepository.findByEmail(username);
        AppUser appUser = queryAppUser.orElseThrow(()-> new BadCredentialsException("No user registered with these details"));

        if(passwordEncoder.matches(password, appUser.getPassword())){
            List<GrantedAuthority> authorities = new ArrayList<>();
            appUser.getRoles().stream().map(authority ->
                    authorities.add(new SimpleGrantedAuthority(authority.name())));

            return new UsernamePasswordAuthenticationToken(username, password, authorities);
        }
        else {
            throw new BadCredentialsException("Invalid password!");
        }
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
