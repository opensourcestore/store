package opensource.onlinestore.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import opensource.onlinestore.LoggedUser;
import opensource.onlinestore.model.authentication.UserAuthentication;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.service.authentication.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by orbot on 29.01.16.
 */
public class LoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    public LoginFilter(String urlMapping, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(urlMapping));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        final UserEntity user = new ObjectMapper().readValue(request.getInputStream(), UserEntity.class);
        final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword()
        );

        return getAuthenticationManager().authenticate(loginToken);
    }

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                          FilterChain chain, Authentication authentication) {
        final LoggedUser authenticatedUser = (LoggedUser)userDetailsService.loadUserByUsername(authentication.getName());
        final UserAuthentication userAuthentication = new UserAuthentication(authenticatedUser);

        tokenAuthenticationService.addAuthentication(response, userAuthentication);

        SecurityContextHolder.getContext().setAuthentication(userAuthentication);
    }
}
