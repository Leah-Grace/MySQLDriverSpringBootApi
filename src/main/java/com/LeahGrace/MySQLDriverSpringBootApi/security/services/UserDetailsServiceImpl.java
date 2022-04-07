package com.LeahGrace.MySQLDriverSpringBootApi.security.services;

import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.User;
import com.LeahGrace.MySQLDriverSpringBootApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
/*
    @Override
    public UserDetails loadUsersByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(("User Not Found: " + username)));

        //we have a user
        return UserDetailsImpl.build(user);
    }

 */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(("User Not Found: " + username)));

        //we have a user
        return UserDetailsImpl.build(user);
    }
}
