package com.LeahGrace.MySQLDriverSpringBootApi.security.services;

import com.LeahGrace.MySQLDriverSpringBootApi.models.auth.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;  //Required for Spring Security //Not showing up as field

    private Long id;

    private String username;

    @JsonIgnore
    private String password; //We have no intention to send this info but if it is eve

    private Collection<? extends GrantedAuthority> authorities; //Must be a collection of objects that qualify as granted authorities class or children of granted authorities class

    public UserDetailsImpl(Long id, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(User user) {  //use your own User as defined in User class, override the user class provided by Springboot
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());  //Creatrs list of granted authority for Spring - Creates collection then turns it into a list of Authorities  // pulls a list of roles, goes through userdeatilsauthorities list to determine what the user has access to
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    public Long getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override  //override with this specific evaluation of two objects for equivalency
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) obj;
        return Objects.equals(id, user.id);
    }
}

/*
Granted Authority is usually a large, complex object that is  most likely a tokenized format object
 */