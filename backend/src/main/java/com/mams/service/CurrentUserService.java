package com.mams.service;

import com.mams.entity.User;
import com.mams.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository users;
    public CurrentUserService(UserRepository users){this.users=users;}
    public User get(){
        String username=SecurityContextHolder.getContext().getAuthentication().getName();
        return users.findByUsername(username).orElseThrow();
    }
    public boolean isAdmin(){return get().getRole().name().equals("ADMIN");}
    public boolean canAccessBase(Long baseId){
        User u=get();
        return u.getRole().name().equals("ADMIN") || (u.getBase()!=null && u.getBase().getId().equals(baseId));
    }
}
