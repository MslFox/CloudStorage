package com.mslfox.cloudStorageServices.security;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.entities.authentication.authorities.UserAuthority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtProvider {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration-in-hours:1}")
    private Long jwtExpirationInHours;

    public String generateJwt(UserDetails userDetails) {
        String username = userDetails.getUsername();
        Date now = new Date();

        Date expiryDate = new Date(now.getTime() + jwtExpirationInHours * 3_600_000);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .claim(ConstantsHolder.JWT_AUTHORITIES_CLAIM_NAME, userDetails.getAuthorities())
                .compact();
    }

    public boolean validateJwt(String jwt) throws Exception {
        Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt);
        return true;
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody();
    }

    public Set<UserAuthority> getAuthorities(String jwt) {
        var claims = getClaims(jwt);
        List<String> authorities = claims.get(ConstantsHolder.JWT_AUTHORITIES_CLAIM_NAME, List.class);
        return authorities.stream().map(UserAuthority::valueOf).collect(Collectors.toSet());
    }

    public String getUsername(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(jwt)
                .getBody();
        return claims.getSubject();
    }

}