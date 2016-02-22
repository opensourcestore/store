package opensource.onlinestore.service.authentication.impl;

import opensource.onlinestore.LoggedUser;
import opensource.onlinestore.model.authentication.UserAuthentication;
import opensource.onlinestore.service.authentication.TokenAuthenticationService;
import opensource.onlinestore.service.authentication.TokenHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by orbot on 29.01.16.
 */
@Service
public class TokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private static final String AUTH_HEADER = "X-AUTH-TOKEN";
    private static final Long ONE_DAY = 1000L * 3600 * 24;

    @Autowired
    private TokenHandler tokenHandler;

    @Override
    public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
        final LoggedUser user = authentication.getDetails();
        user.setExpires(System.currentTimeMillis() + ONE_DAY);
        response.addHeader(AUTH_HEADER, tokenHandler.createTokenForUser(user));
    }

    @Override
    public Authentication getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER);
        if(token != null) {
            final LoggedUser user = tokenHandler.parseUserFromToken(token);
            if(user != null) {
                return new UserAuthentication(user);
            }
        }
        return null;
    }
}
