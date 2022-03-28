package com.phoenix.auctionsystem.data.repository;

import com.phoenix.auctionsystem.data.model.AppUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {

      Optional<AppUser> findByEmail(String email);

      boolean existsByEmail(String email);
}
