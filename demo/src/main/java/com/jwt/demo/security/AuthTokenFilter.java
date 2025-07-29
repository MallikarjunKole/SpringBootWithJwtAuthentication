package com.jwt.demo.security;


import com.jwt.demo.registration.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Value("${jwt.tokenHeader}")
  private String header;

  @Value("${jwt.tokenStartsWith}")
  private String headerStartsWithValue;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

 @Override
 protected void doFilterInternal(HttpServletRequest request,
                                 HttpServletResponse response,
                                 FilterChain filterChain)
         throws ServletException, IOException {
   String path = request.getServletPath();

   if (path.equals("/api/auth/login") || path.equals("/api/auth/register")) {
     filterChain.doFilter(request, response);
     return;
   }

   final String authHeader = request.getHeader(header);
   String jwt = null;
   String username = null;

   // Step 1: Extract JWT token
   System.out.println("authHeader : "+authHeader);
   if (authHeader != null && authHeader.startsWith(headerStartsWithValue)) {
     jwt = authHeader.substring(7);
     try{
     username = jwtUtils.extractUsername(jwt);
     } catch (IllegalArgumentException e) {
       System.out.println("Unable to get JWT Token");
     } catch (ExpiredJwtException e) {
       System.out.println("JWT Token has expired");
     }
   } else {
    logger.warn("JWT Token does not begin with Bearer String");
  }

   // Step 2: Validate token and set SecurityContext
   if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
     UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
     if (jwtUtils.validateToken(jwt, userDetails.getUsername())) {
       UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
               userDetails, null, userDetails.getAuthorities()
       );
       authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
       SecurityContextHolder.getContext().setAuthentication(authToken);
     }
   }

   // Step 3: Continue filter chain
   filterChain.doFilter(request, response);
 }

}
