package de.zebrajaeger.panostore.service;

import de.zebrajaeger.panostore.auth.AuthoritationUtils;
import de.zebrajaeger.panostore.data.ApplicationUser;
import de.zebrajaeger.panostore.repo.ApplicationUserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {
    private ApplicationUserRepository applicationUserRepository;

    public UserDetailsServiceImpl(ApplicationUserRepository applicationUserRepository) {
        this.applicationUserRepository = applicationUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = applicationUserRepository.findByUsername(username);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(username);
        }

        return new User(
                applicationUser.getUsername(),
                applicationUser.getPassword(),
                AuthoritationUtils.readRoles(applicationUser));
    }
}