package com.mslfox.cloudStorageServices.entities.jwt;

import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "black_jwt")
public class BlackJwtEntity {

    @Column
    private String token;

    @Id
    private Long expiration;

}
