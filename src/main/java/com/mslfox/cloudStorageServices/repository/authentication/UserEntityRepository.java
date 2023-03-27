package com.mslfox.cloudStorageServices.repository.authentication;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.entities.authentication.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> , UserDetailsService {
    @Override
    default UserEntity loadUserByUsername(String username) throws UsernameNotFoundException {
        return  findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(ConstantsHolder.ERROR_USER_NOT_FOUND));
    }

    Optional<UserEntity> findByUsername(String username);

}
