package com.thales.oauth.security;

import com.thales.oauth.domain.UserSystem;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UsuarioSistema extends User {

    private static final long serialVersionUID = 1L;

    private UserSystem user;

    public UsuarioSistema(UserSystem user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getLogin(), user.getPassword(), authorities);
        this.user = user;
    }

    public UserSystem getUser() {
        return user;
    }
}