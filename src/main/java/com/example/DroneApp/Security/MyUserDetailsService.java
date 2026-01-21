package com.example.DroneApp.Security;

import com.example.DroneApp.Model.User;
import com.example.DroneApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private static final Logger logger =
            Logger.getLogger(MyUserDetailsService.class.getName());

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        User user = userService.findUserByEmailAddress(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        logger.info("Authenticating user: " + email + " with role: " + user.getRole());

        return new MyUserDetails(user);
    }
}
