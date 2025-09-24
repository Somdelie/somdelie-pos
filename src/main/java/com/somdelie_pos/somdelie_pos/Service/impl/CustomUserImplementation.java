package com.somdelie_pos.somdelie_pos.Service.impl;

import com.somdelie_pos.somdelie_pos.modal.User;
import com.somdelie_pos.somdelie_pos.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class CustomUserImplementation  implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User with this email does not exist");
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(
                user.getRole().toString()
        );

        Collection<GrantedAuthority> authorities =
                Collections.singleton(authority);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), authorities
        );
    }
}
