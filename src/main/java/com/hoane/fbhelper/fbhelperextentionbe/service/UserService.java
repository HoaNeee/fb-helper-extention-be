package com.hoane.fbhelper.fbhelperextentionbe.service;


import com.hoane.fbhelper.fbhelperextentionbe.dto.request.UserRequest;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ConflictException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.UserRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User findByIdOrThrow(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(String userId, UserRequest.UserUpdateRequest userUpdateRequest) {
        User user = findByIdOrThrow(userId);

        String email = userUpdateRequest.getEmail();
        String username = userUpdateRequest.getUsername();

        if (email != null && !email.equals(user.getEmail())) {
            if (isExistedEmail(email)) {
                throw new ConflictException("Email already exists");
            }
        }

        if (username != null && !username.equals(user.getUsername())) {
            if (isExistedUsername(username)) {
                throw new ConflictException("Username already exists");
            }
        }

        modelMapper.map(userUpdateRequest, user);

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(String userId, UserRequest.UserChangePasswordRequest userChangePasswordRequest) {
        User user = findByIdOrThrow(userId);

        String oldPassword = userChangePasswordRequest.getOldPassword();
        String newPassword = userChangePasswordRequest.getNewPassword();

        boolean isPasswordValid = passwordEncoder.matches(oldPassword, user.getPassword());

        if (!isPasswordValid) {
            throw new RuntimeException("Invalid old password");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public String updateRole(UserRequest.UserUpdateRoleRequest userUpdateRoleRequest) {
        String userId = userUpdateRoleRequest.getUserId();

        if (!SecurityContextUtils.isAdminRoleFromContext()) {
            throw new AuthorizationDeniedException("Not Allowed");
        }

        Role role = userUpdateRoleRequest.getRole();

        if (role.equals(Role.ROLE_ADMIN) && !SecurityContextUtils.isSuperAdminRoleFromContext()) {
            throw new AuthorizationDeniedException("Not Allowed to assign ROLE_ADMIN");
        }

        User user = findByIdOrThrow(userId);

        user.setRole(role);

        userRepository.save(user);
        return role.toString();
    }

    @Transactional
    public String updateStatus(UserRequest.UserUpdateStatusRequest userUpdateStatusRequest) {
        String userId = userUpdateStatusRequest.getUserId();
        if (!SecurityContextUtils.isAdminRoleFromContext()) {
            throw new AuthorizationDeniedException("Not Allowed");
        }

        User user = findByIdOrThrow(userId);

        user.setStatus(userUpdateStatusRequest.getStatus());

        userRepository.save(user);
        return user.getStatus().toString();
    }

    public boolean isMember() {
        return SecurityContextUtils.isMemberRoleFromContext();
    }

    public boolean isAdmin() {
        return SecurityContextUtils.isAdminRoleFromContext();
    }

    public boolean isSuperAdmin() {
        return SecurityContextUtils.isSuperAdminRoleFromContext();
    }

    private boolean isExistedEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    private boolean isExistedUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }


}
