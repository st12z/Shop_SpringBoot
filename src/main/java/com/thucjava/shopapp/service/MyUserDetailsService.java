package com.thucjava.shopapp.service;

import com.thucjava.shopapp.model.User;
import com.thucjava.shopapp.model.UserPrincipal;
import com.thucjava.shopapp.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user=userRepo.findByEmail(username);
        if(user==null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }
}
