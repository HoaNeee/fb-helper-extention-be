package com.hoane.fbhelper.fbhelperextentionbe.service;

import com.hoane.fbhelper.fbhelperextentionbe.dto.request.LoginRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RegisterRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.request.RequestModels;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.LoginResponse;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.RegisterResponse;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.RequestPlatform;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserExistException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.UserRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.IdGenerator;
import com.hoane.fbhelper.fbhelperextentionbe.utils.JwtUtils;
import com.hoane.fbhelper.fbhelperextentionbe.utils.MyRequestContext;
import com.hoane.fbhelper.fbhelperextentionbe.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new UserExistException("Email already exists");
        }

        // Create new user
        User user = User.builder()
                .username(registerRequest.getUsername())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(Role.ROLE_USER)
                .isNewUser(true)
                .status(UserStatus.PENDING)
                .id(IdGenerator.generateId(10))
                .build();

        User saved = userRepository.save(user);

        return new RegisterResponse(saved.getUsername(), saved.getEmail(), saved.getStatus());
    }

    public LoginResponse login(LoginRequest request, String ipAddress) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        RequestPlatform requestPlatform = MyRequestContext.getRequestPlatform();

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        if (userDetails == null) {
            throw new UserNotFoundException("User not found");
        }


        if (requestPlatform.equals(RequestPlatform.DEVICE) || requestPlatform.equals(RequestPlatform.WEB)) {
            if (userDetails.isSuperAdmin()) {
                throw new UserNotFoundException("User not found");
            }
        }

        if (userDetails.getIsNewUser() && !userDetails.isSuperAdmin()) {
            handleForNewUser(userDetails.getId());
        }

        if (ipAddress != null && requestPlatform.equals(RequestPlatform.DEVICE)) {
            ipAddressService.findByIpAddressOrCreate(ipAddress);
        }

        String token = jwtUtils.generateToken(authentication);

        LoginResponse response = new LoginResponse(token, userDetails.getId(), userDetails.getUsername(), userDetails.getUserStatus());

        if (requestPlatform.equals(RequestPlatform.ADMIN) && !userDetails.isAdmin()) {
            throw new UserNotFoundException("User not found");
        }

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
