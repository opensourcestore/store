package opensource.onlinestore.model.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by maks(avto12@i.ua) on 26.01.2016.
 */

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    CONTENT_MANAGER,
    SUPPORT;

    @Override
    public String getAuthority() {
        return name();
    }

}
