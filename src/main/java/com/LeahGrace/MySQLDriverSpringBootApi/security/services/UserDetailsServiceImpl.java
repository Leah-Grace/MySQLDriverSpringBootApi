package com.LeahGrace.MySQLDriverSpringBootApi.security.services;

import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.User;
import com.LeahGrace.MySQLDriverSpringBootApi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service // If its not a service its a component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository; // Import your user repository
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
    } //Tell Springboot how to find your users, pass the UsernameNotFoundException, use UserDetailsImpl to build the userdetails according to the UserRepository
}
