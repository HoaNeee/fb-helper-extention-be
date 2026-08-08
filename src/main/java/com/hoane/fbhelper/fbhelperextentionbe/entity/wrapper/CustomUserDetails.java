package com.hoane.fbhelper.fbhelperextentionbe.entity.wrapper;

import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.Role;
import com.hoane.fbhelper.fbhelperextentionbe.entity.enums.UserStatus;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        return List.of(authority);
    }

    @Override
    public @Nullable String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public UserStatus getUserStatus() {
        return this.user.getStatus();
    }

    public Role getRole() {
        return this.user.getRole();
    }

    @Override
    public boolean isEnabled() {
        return this.user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.user.isStatus(UserStatus.LOCKED);
    }

    public String getId() {
        return this.user.getId();
    }

    public boolean getIsNewUser() {
        return this.user.getIsNewUser();
    }

}
