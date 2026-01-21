package com.example.DroneApp.Security;

import com.example.DroneApp.Enum.RoleEnum;
import com.example.DroneApp.Model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

public class MyUserDetails implements UserDetails {

    private static final Logger logger = Logger.getLogger(MyUserDetails.class.getName());

    private User user;

    public MyUserDetails(User user) {
        this.user = user;
        logger.info("Loaded user: " + user.getEmailAddress() + " with role: " + user.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();
        RoleEnum role = user.getRole();
        if (role != null) {
            authorities.add(new SimpleGrantedAuthority(role.name()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmailAddress();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return user;
    }
}
