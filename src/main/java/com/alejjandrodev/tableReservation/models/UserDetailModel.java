package com.alejjandrodev.tableReservation.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class UserDetailModel implements UserDetails {

    private String username;
    private String password;
    private ServerUser user;
    private List<GrantedAuthority> authorities;

    public UserDetailModel(ServerUser user){
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = new LinkedList<GrantedAuthority>();
        this.authorities.add(new SimpleGrantedAuthority("ADMIN"));
        this.user = user;
    }

    public ServerUser getUser() {
        return user;
    }

    public void setUser(ServerUser user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return this.authorities; }

    @Override
    public String getPassword() { return this.password; }

    @Override
    public String getUsername() { return this.username; }

    @Override
    public boolean isAccountNonExpired() { return false; }

    @Override
    public boolean isAccountNonLocked() { return false; }

    @Override
    public boolean isCredentialsNonExpired() { return false; }

    @Override
    public boolean isEnabled() { return false; }

}