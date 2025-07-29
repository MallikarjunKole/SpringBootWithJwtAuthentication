package com.jwt.demo.registration.controller;

import com.jwt.demo.global.constants.ResponseCodes;
import com.jwt.demo.global.dto.ErrorInfo;
import com.jwt.demo.global.dto.Response;
import com.jwt.demo.registration.domain.User;
import com.jwt.demo.registration.dto.LoginRequest;
import com.jwt.demo.registration.dto.RegistrationResponse;
import com.jwt.demo.registration.service.RegistrationService;
import com.jwt.demo.registration.serviceImpl.UserDetailsImpl;
import com.jwt.demo.security.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    RegistrationService registrationService;

    @Autowired
    JwtUtils jwtUtil;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        RegistrationResponse response = new RegistrationResponse();
        try {
            logger.info("start "+request.toString());
            Optional<User> user = registrationService.findByUsername(request.getUsername());
            System.out.println("user : "+user.toString());

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if ((userDetails.getUsername().equals(request.getUsername()) && encoder
                    .matches(request.getPassword(), userDetails.getPassword()))
            ) {
                final String token = jwtUtil.generateJwtToken(userDetails);
                response.setMessage("User authenticated successfully.");
                response.setStatus(ResponseCodes.SUCCESS);
                response.setTimestamp(new Date().toString());
                response.setToken(token);
                response.setResponse(userDetails);
                return ResponseEntity.ok(Response.success("Login successful", response));
            }
            response.setMessage("User authentication failed.");
            response.setStatus(ResponseCodes.ERROR);
            response.setTimestamp(new Date().toString());

        } catch (BadCredentialsException ex) {
            ErrorInfo errorInfo = new ErrorInfo(
                    HttpStatus.NOT_FOUND.value(),
                    HttpStatus.NOT_FOUND.getReasonPhrase(),
                    ex.getMessage(),
                    "USR_404"
            );
            return ResponseEntity.ok(Response.failure("User Not Found", errorInfo));
        } catch (UsernameNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        }catch (Exception e) {
            response.setMessage("User authentication failed.");
            response.setStatus(ResponseCodes.ERROR);
            response.setTimestamp(new Date().toString());
            response.setError(e);
        }

        return ResponseEntity.ok(response);
    }

}
