package com.terzo.portal.dto;

import com.terzo.portal.entity.Role;
import com.terzo.portal.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsDTO implements UserDetails {
    private int id;
    private String email;
    private String password;
    private boolean active;
    private List<GrantedAuthority> authorities;
    private Role role;

    public UserDetailsDTO(){}

    public UserDetailsDTO(User user) {
        this.id= user.getId();
        this.email= user.getEmail();
        this.password= user.getPassword();
        this.active= user.isActive();
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        this.role = user.getRole();
        authorities.add(new SimpleGrantedAuthority(role.getName()));
        this.authorities=authorities;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return active;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "UserDetailsDTO{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", authorities=" + authorities +
                ", roles=" + role +
                '}';
    }

}
