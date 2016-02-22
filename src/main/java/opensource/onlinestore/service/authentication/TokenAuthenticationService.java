package opensource.onlinestore.service.authentication;

import opensource.onlinestore.model.authentication.UserAuthentication;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by orbot on 29.01.16.
 */
public interface TokenAuthenticationService {
    void addAuthentication(HttpServletResponse response, UserAuthentication authentication);

    Authentication getAuthentication(HttpServletRequest request);
}
