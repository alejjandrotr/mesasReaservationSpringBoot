package com.alejjandrodev.tableReservation.services;

import com.alejjandrodev.tableReservation.models.ServerUser;
import com.alejjandrodev.tableReservation.models.UserDetailModel;
import com.alejjandrodev.tableReservation.repositories.ServerUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ServerUserRepository serverUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<ServerUser> user = serverUserRepository.findByEmail(username);
        return user.map(UserDetailModel::new).orElseThrow(()->new UsernameNotFoundException("Invalid Username"));
    }
}