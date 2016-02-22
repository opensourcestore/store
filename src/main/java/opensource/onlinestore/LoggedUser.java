package opensource.onlinestore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import opensource.onlinestore.model.dto.UserDTO;
import opensource.onlinestore.model.enums.ActivityStatus;
import opensource.onlinestore.model.enums.Role;
import opensource.onlinestore.model.entity.UserEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Created by maks(avto12@i.ua) on 27.01.2016.
 */
public class LoggedUser implements UserDetails {
    private UserDTO user;
    private Long expires;
    private Set<Role> roles;
    private boolean accountBlocked;

    public LoggedUser() {
    }

    public LoggedUser(UserEntity user) {
        this.user = new UserDTO();
        this.user.setEmail(user.getEmail());
        this.user.setPassword(user.getPassword());
        this.user.setLastName(user.getLastName());
        this.user.setFirstName(user.getFirstName());
        this.user.setUsername(user.getUsername());
        this.accountBlocked = user.getActivityStatus() == ActivityStatus.BANNED;
        this.roles = user.getRoles();
    }

    public static LoggedUser safeGet() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return null;
        }
        Object user = auth.getPrincipal();
        return (user instanceof LoggedUser) ? (LoggedUser) user : null;
    }

    public static LoggedUser get() {
        LoggedUser user = safeGet();
        requireNonNull(user, "No authorized user found");
        return user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void update(UserDTO newTo) {
        user = newTo;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    @Override
    public String toString() {
        return user.toString();
    }


    @Override
    @JsonIgnore
    public Collection<GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(role -> (GrantedAuthority)role)
                .collect(Collectors.toSet());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !accountBlocked;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}
