package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.LoginRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RegisterRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.LoginResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.RegisterResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserExistException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.UserRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentPostService commentPostService;

    @Autowired
    private InteractBeforePostService interactBeforePostService;

    public RegisterResponse register(RegisterRequest registerRequest) {
        // Check if username already exists
        if (userRepository.existsUserByUsername(registerRequest.getUsername())) {
            throw new UserExistException("Username already exists");
        }

        // Create new user
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getUsername(), saved.getEmail(), saved.getStatus());
    }

    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails.getIsNewUser()) {
            handleForNewUser(userDetails.getId());
        }

        String token = jwtUtils.generateToken(authentication);
        return new LoginResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getRole(), userDetails.getUserStatus());
    }

    public String getUserIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getId();
            }
        }
        return null;
    }

    public void handleForNewUser(String userId) {
        User user = userService.findByIdOrThrow(userId);

        if (!user.getIsNewUser()) {
            return;
        }

        commentPostService.createNewCommentPostForUser(user.getId());
        interactBeforePostService.createInteractBeforePostForUser(user.getId());

        user.setIsNewUser(false);
        userRepository.save(user);
    }
}
