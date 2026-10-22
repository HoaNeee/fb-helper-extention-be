package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.LoginRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RegisterRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.LoginResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.RegisterResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserExistException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserNotFoundException;
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
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private CommentPostService commentPostService;

    @Autowired
    private IpAddressService ipAddressService;

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

    public LoginResponse login(LoginRequest request, String ipAddress) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            throw new UserNotFoundException("User not found");
        }

        if (userDetails.getIsNewUser() && (userDetails.getRole().equals(Role.ROLE_USER) || userDetails.getRole().equals(Role.ROLE_ADMIN))) {
            handleForNewUser(userDetails.getId());
        }

        if (ipAddress != null) {
            ipAddressService.findByIpAddressOrCreate(ipAddress);
        }

        String token = jwtUtils.generateToken(authentication);

        LoginResponse response = new LoginResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getUserStatus());

        if (userDetails.isAdmin()) {
            response.setRole(userDetails.getRole());
        }

        return response;
    }


    public boolean verifyToken(RequestModels.VerifyTokenRequest request, String ipAddress) {
        String token = request.token();
        if (token == null || token.isEmpty()) return false;
        try {
            jwtUtils.validateJwtToken(token);

            if (ipAddress != null) {
                ipAddressService.findByIpAddressOrCreate(ipAddress);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public void handleForNewUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getIsNewUser() || user.isRole(Role.ROLE_SUPER_ADMIN)) {
            return;
        }

        commentPostService.createNewCommentPostForUser(userId);
        interactBeforePostService.createInteractBeforePostForUser(userId);

        user.setIsNewUser(false);
        userRepository.save(user);
    }
}
