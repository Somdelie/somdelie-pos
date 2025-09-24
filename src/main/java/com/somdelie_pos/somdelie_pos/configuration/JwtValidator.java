package com.somdelie_pos.somdelie_pos.configuration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

/**
 * This is like a custom Express middleware:
 *
 * In Express, you'd write:
 *   app.use("/api", (req, res, next) => {
 *       const token = req. Headers["authorization"];
 *       // validate token...
 *       next();
 *   })
 *
 * In Spring Boot:
 * - Extends OncePerRequestFilter → ensures it runs once per request.
 * - Intercepts the request before it hits controllers.
 * - Reads the "Authorization" header.
 * - If Bearer token exists → extract it (stub for now).
 * - Passes control to the next filter/controller.
 */
public class JwtValidator extends OncePerRequestFilter {

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
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.JWT_SECRET.getBytes());
                Claims claims = Jwts.parser()
                        .verifyWith(key)
                        .build()
                        .parseSignedClaims(token)  // ✅ use the stripped token
                        .getPayload();

                String email = String.valueOf(claims.get("email"));
                String authorities = String.valueOf(claims.get("authorities"));

                List<GrantedAuthority> auths =
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication auth = new UsernamePasswordAuthenticationToken(
                        email, null, auths);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            catch (Exception e) {
                throw new BadCredentialsException("Invalid JWT token...!");
            }
        }

        // Continue request pipeline (like calling next() in Express)
        filterChain.doFilter(request, response);
    }
}
