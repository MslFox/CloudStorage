package com.mslfox.cloudStorageServices.entities.authentication;

import com.mslfox.cloudStorageServices.entities.authentication.authorities.UserAuthority;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collections;
import java.util.Set;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", indexes = @Index(columnList = "username asc"))
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    private Set<UserAuthority> authorities = Collections.singleton(UserAuthority.ROLE_USER);
    @Column
    public boolean accountNonExpired;
    @Column
    public boolean accountNonLocked;
    @Column
    public boolean credentialsNonExpired;
    @Column
    public boolean enabled;

    public UserEntity(String username, String password) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.singleton(UserAuthority.ROLE_USER);
        this.accountNonExpired = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;
        this.enabled = true;
    }


    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


}
