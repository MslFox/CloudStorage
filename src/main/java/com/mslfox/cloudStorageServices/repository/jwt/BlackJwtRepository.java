package com.mslfox.cloudStorageServices.repository.jwt;

import com.mslfox.cloudStorageServices.entities.jwt.BlackJwtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BlackJwtRepository extends JpaRepository<BlackJwtEntity, Long> {
    void deleteByExpirationBefore(Long expiration);
}
