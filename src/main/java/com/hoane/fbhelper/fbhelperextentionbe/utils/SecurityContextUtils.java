package com.hoane.fbhelper.fbhelperextentionbe.utils;

import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class SecurityContextUtils {
    public static String getUserIdFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return ((CustomUserDetails) principal).getId();
            }
        }
        return null;
    }

    public static CustomUserDetails getUserDetailsFromContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                return (CustomUserDetails) principal;
            }
        }
        return null;
    }

    public static Role getRoleFromContext() {
        CustomUserDetails userDetails = getUserDetailsFromContext();
        if (userDetails == null) return Role.ROLE_USER;
        return userDetails.getRole();
    }

    public static boolean isAdminRoleFromContext() {
        CustomUserDetails userDetails = getUserDetailsFromContext();
        if (userDetails == null) return false;
        return userDetails.getRole().equals(Role.ROLE_ADMIN) || userDetails.getRole().equals(Role.ROLE_SUPER_ADMIN);
    }

    public static boolean isMemberRoleFromContext() {
        CustomUserDetails userDetails = getUserDetailsFromContext();
        if (userDetails == null) return false;
        return userDetails.getRole().equals(Role.ROLE_MEMBER) || isAdminRoleFromContext() || isSuperAdminRoleFromContext();
    }

    public static boolean isSuperAdminRoleFromContext() {
        CustomUserDetails userDetails = getUserDetailsFromContext();
        if (userDetails == null) return false;
        return userDetails.getRole().equals(Role.ROLE_SUPER_ADMIN);
    }
}
