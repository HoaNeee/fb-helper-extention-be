package com.hoane.fbhelper.fbhelperextentionbe.service;


import com.hoane.fbhelper.fbhelperextentionbe.dto.request.UserRequest;
import com.hoane.fbhelper.fbhelperextentionbe.dto.response.ResponseModels;
import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import com.hoane.fbhelper.fbhelperextentionbe.exception.ConflictException;
import com.hoane.fbhelper.fbhelperextentionbe.exception.UserNotFoundException;
import com.hoane.fbhelper.fbhelperextentionbe.repository.UserRepository;
import com.hoane.fbhelper.fbhelperextentionbe.utils.IdGenerator;
import com.hoane.fbhelper.fbhelperextentionbe.utils.SecurityContextUtils;
import com.hoane.fbhelper.fbhelperextentionbe.utils.Utils;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public User create(UserRequest.CreateUserRequest request) {

        String email = request.getEmail();
        String username = request.getUsername();

        if (isExistedEmail(email)) {
            throw new ConflictException("Email already exists");
        }

        if (isExistedUsername(username)) {
            throw new ConflictException("Username already exists");
        }

        User user = User.builder()
                .status(request.getStatus() != null ? UserStatus.valueOf(request.getStatus()) : UserStatus.PENDING)
                .role(request.getRole() != null ? Role.valueOf(request.getRole()) : Role.ROLE_USER)
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .id(IdGenerator.generateId(10))
                .build();

        userRepository.save(user);

        return user;
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
    public User adminUpdate(String userId, UserRequest.AdminUpdateUserRequest userUpdateRequest) {

        CustomUserDetails currentUser = SecurityContextUtils.getUserDetailsFromContext();

        User user = findByIdOrThrow(userId);

        if (currentUser == null || currentUser.getId().equalsIgnoreCase(userId)) {
            throw new RuntimeException("Admin can not update their own role and status");
        }

        String email = userUpdateRequest.getEmail();
        String username = userUpdateRequest.getUsername();
        Role role = Role.valueOf(userUpdateRequest.getRole());
        UserStatus status = UserStatus.valueOf(userUpdateRequest.getStatus());

        if (getNumberOfRole(currentUser.getRole()) >= getNumberOfRole(role)) {
            throw new AuthorizationDeniedException("Not Allowed to assign role higher than your role");
        }


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
        
        user.setRole(role);
        user.setStatus(status);
        user.setEmail(email);
        user.setUsername(username);

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

        Role role = Role.valueOf(userUpdateRoleRequest.getRole());
        Role currentRole = SecurityContextUtils.getRoleFromContext();

        if (getNumberOfRole(currentRole) >= getNumberOfRole(role)) {
            throw new AuthorizationDeniedException("Not Allowed to assign role higher than your role");
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

        user.setStatus(UserStatus.valueOf(userUpdateStatusRequest.getStatus()));

        userRepository.save(user);
        return user.getStatus().toString();
    }

    public Page<User> getAllUser(Pageable pageable) {
        return userRepository.findAll(pageable);
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


    public ResponseModels.AdminStatisticUserResponse getAdminStatistics() {
        Integer countUserActive = userRepository.countByStatus(UserStatus.ACTIVE);
        Integer countUserInactive = userRepository.countByStatus(UserStatus.INACTIVE);
        Integer countUserLocked = userRepository.countByStatus(UserStatus.LOCKED);
        Integer countUserPending = userRepository.countByStatus(UserStatus.PENDING);

        return new ResponseModels.AdminStatisticUserResponse(countUserActive, countUserInactive, countUserLocked, countUserPending);
    }

    public Boolean deleteUser(String userId) {
        User u = findByIdOrThrow(userId);
        u.setIsDeleted(true);
        userRepository.save(u);
        return true;
    }

    private boolean isExistedEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    private boolean isExistedUsername(String username) {
        return userRepository.existsUserByUsername(username);
    }


    private Integer getNumberOfRole(Role role) {
        switch (role) {
            case ROLE_SUPER_ADMIN:
                return 0;
            case ROLE_ADMIN:
                return 1;
            case ROLE_MEMBER:
                return 2;
            default:
                return 3;
        }
    }

}
