package backend.FinSight.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.User;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getServletPath();

        // SKIP JWT FILTER FOR AUTH ROUTES

        if (
                path.startsWith("/auth/")
                        || path.startsWith("/oauth2/")
                        || path.startsWith("/login/")
        ) {

            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader =
                request.getHeader("Authorization");

        String username = null;

        String token = null;

        // CHECK AUTH HEADER

        if (
                authHeader != null
                        && authHeader.startsWith("Bearer ")
        ) {

            token = authHeader.substring(7);

            try {

                username =
                        jwtService.extractUsername(token);

            } catch (Exception e) {

                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );

                response.getWriter().write("Invalid JWT Token");

                return;
            }
        }

        // SET AUTHENTICATION

        if (
                username != null
                        && SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null
        ) {

            User userDetails =
                    new User(
                            username,
                            "",
                            Collections.emptyList()
                    );

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            authToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request)
            );

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }
}