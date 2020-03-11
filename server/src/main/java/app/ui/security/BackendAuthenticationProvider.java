package app.ui.security;

import app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class BackendAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(BackendAuthenticationProvider.class);
    private final UserService userService;

    public BackendAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var login = authentication.getName();
        var password = authentication.getCredentials().toString();
        var user = userService.getUserDetailsAfterAuthentication(login, password);
        if (user == null) {
            throw new BadCredentialsException("wrong username or password");
        }
        var grantedAuthorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(login, password, grantedAuthorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
