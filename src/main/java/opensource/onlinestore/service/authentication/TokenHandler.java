package opensource.onlinestore.service.authentication;

import opensource.onlinestore.LoggedUser;
import opensource.onlinestore.model.entity.UserEntity;

/**
 * Created by orbot on 29.01.16.
 */
public interface TokenHandler {
    LoggedUser parseUserFromToken(String token);

    String createTokenForUser(LoggedUser user);
}
