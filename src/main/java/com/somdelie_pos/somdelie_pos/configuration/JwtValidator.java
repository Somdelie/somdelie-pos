package com.somdelie_pos.somdelie_pos.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * This is like a custom Express middleware:
 *
 * In Express, you'd write:
 *   app.use("/api", (req, res, next) => {
 *       const token = req.headers["authorization"];
 *       // validate token...
 *       next();
 *   })
 *
 * In Spring Boot:
 * - Extends OncePerRequestFilter → ensures it runs once per request.
 * - Intercepts the request before it hits controllers.
 * - Reads the "Authorization" header.
 * - If Bearer token exists → extract it and validate using JwtProvider.
 * - Passes control to the next filter/controller.
 */
@Component
public class JwtValidator extends OncePerRequestFilter {

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Grab the Authorization header (like req.headers["authorization"] in Express)
        String jwt = request.getHeader(JwtConstant.JWT_HEADER);

        if (jwt != null && jwt.startsWith("Bearer ")) {
            String token = jwt.substring(7); // remove "Bearer "
            try {
                // Use JwtProvider for consistent validation
                if (jwtProvider.validate(token)) {
                    String email = jwtProvider.getEmail(token);
                    Set<String> authoritiesSet = jwtProvider.getAuthorities(token);
                    
                    // Convert Set to comma-separated string for AuthorityUtils
                    String authoritiesStr = String.join(",", authoritiesSet);
                    List<GrantedAuthority> auths = 
                            AuthorityUtils.commaSeparatedStringToAuthorityList(authoritiesStr);

                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            email, null, auths);

                    SecurityContextHolder.getContext().setAuthentication(auth);
                } else {
                    throw new BadCredentialsException("Invalid JWT token...!");
                }
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid JWT token...!");
            }
        }

        // Continue request pipeline (like calling next() in Express)
        filterChain.doFilter(request, response);
    }
}
