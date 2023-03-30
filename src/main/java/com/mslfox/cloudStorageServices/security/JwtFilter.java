package com.mslfox.cloudStorageServices.security;

import com.mslfox.cloudStorageServices.constant.ConstantsHolder;
import com.mslfox.cloudStorageServices.model.error.ErrorResponse;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        if (!httpServletRequest.getRequestURI().equals("/login")) {
            try {
                String jwt = getJwt(httpServletRequest);
                if (StringUtils.hasText(jwt) && jwtProvider.validateJwt(jwt)) {
                    final var authentication = new UsernamePasswordAuthenticationToken(
                            jwtProvider.getUsername(jwt), null, jwtProvider.getAuthorities(jwt));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.error(e.getMessage());
                var errorResponse = new ErrorResponse(e.getMessage(), 1L);
                httpServletResponse.getWriter().write(errorResponse.toJsonString());
                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    public String getJwt(HttpServletRequest request) throws SignatureException {
        String authHeader = request.getHeader(ConstantsHolder.TOKEN_HEADER_NAME);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

}