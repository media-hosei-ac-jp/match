package jp.co.sogeninc.semv2_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jp.co.sogeninc.semv2_be.domain.User;
import lombok.Setter;

@Service
@ConfigurationProperties(prefix="semv2")
public class AdminLoginUserDetailsService implements UserDetailsService {

    @Setter
    private String adminPassword;

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String uid)
            throws UsernameNotFoundException {
        User login = userService.findByUid(uid);
        if(login != null) {
        	login.setPassword(adminPassword);
            return login;
        }

        throw new UsernameNotFoundException("The requested user is not found");
    }


}
