package com.ead.authuser.configs.security;

import com.ead.authuser.models.UserModel;
import com.ead.authuser.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userRepository.findByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found with username: " + username)
        );

        return UserDetailsImpl.build(userModel);
    }

    public UserDetails loadUserById(UUID userId) {
        UserModel userModel = userRepository.findById(userId).orElseThrow(() ->
                new AuthenticationCredentialsNotFoundException("User not found with userId: " + userId)
        );

        return UserDetailsImpl.build(userModel);
    }
}
