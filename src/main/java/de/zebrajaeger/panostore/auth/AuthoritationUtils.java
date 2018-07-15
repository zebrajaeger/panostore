package de.zebrajaeger.panostore.auth;

import de.zebrajaeger.panostore.data.ApplicationUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.stream.Collectors;

public class AuthoritationUtils {
    private AuthoritationUtils() {
    }

    public static Collection<SimpleGrantedAuthority> readRoles(ApplicationUser user){
        return  user.getRoles().stream()
                .map( role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toSet());

    }
}
