package com.nofear.nac.filter;

import com.nofear.nac.entities.Token;
import com.nofear.nac.exception.JwtValidationException;
import com.nofear.nac.repositories.TokenRepository;
import com.nofear.nac.services.JWTServiceImpl;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JWTServiceImpl jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {

            if (httpRequest.getServletPath().contains("/api/v1/auth")) {
                System.out.println("----------------- JwtAuthenticationFilter::doFilter - Will forward this API to authent use UserName-Password ----------------- ");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("----------------- JwtAuthenticationFilter::doFilter - Process Authentication via JWT Token ----------------- ");

            final String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            final String userName = jwtService.extractUsername(jwt);

            Token storedToken =  tokenRepository.findByToken(jwt).orElseThrow();
            boolean isValidToken = !storedToken.isExpired();

            if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null && isValidToken) {

                UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

                if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {
                    System.out.println("----------------- JwtAuthenticationFilter::doFilter User is authenticated via JWT token " + userDetails + " ----------------- ");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            handleException(httpResponse, ex, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void handleException(HttpServletResponse response, Exception ex, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        System.out.println(ex.getMessage());
        String message = "Invalid Authentication";
        response.getWriter().write("{\"error\":\"" +message  + "\"}");
    }
}
