package opensource.onlinestore.service.impl;

import opensource.onlinestore.LoggedUser;
import opensource.onlinestore.Utils.Exceptions.NotFoundException;
import opensource.onlinestore.Utils.UserUtils;
import opensource.onlinestore.model.authentication.UserAuthentication;
import opensource.onlinestore.model.dto.UserDTO;
import opensource.onlinestore.model.enums.ActivityStatus;
import opensource.onlinestore.model.enums.Role;
import opensource.onlinestore.model.entity.UserEntity;
import opensource.onlinestore.repository.UserRepository;
import opensource.onlinestore.service.UserService;
import opensource.onlinestore.service.authentication.TokenHandler;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * Created by maks(avto12@i.ua) on 27.01.2016.
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenHandler tokenHandler;
    @Autowired
    private Mapper beanMapper;

    @Override
    public LoggedUser loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity u = userRepository.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("User " + username + " is not found");
        return new LoggedUser(u);
    }

    @Override
    public UserEntity registerNewUser(UserDTO userDTO) {
        UserEntity newUser = beanMapper.map(userDTO, UserEntity.class);
        newUser.setActivityStatus(ActivityStatus.ACTIVE);
        newUser.addRole(Role.USER);
        newUser = UserUtils.prepareToSave(newUser);
        userRepository.save(newUser);
        return newUser;
    }

    @Override
    public String authenticateUserAndGetToken(UserEntity user) {
        LoggedUser loggedUser = new LoggedUser(user);
        UserAuthentication authentication = new UserAuthentication(loggedUser);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenHandler.createTokenForUser(loggedUser);
        return token;
    }

    public UserEntity save(UserEntity user) {
        return userRepository.saveAndFlush(user);
    }

    public void delete(Long id) throws NotFoundException {
        userRepository.delete(id);
    }

    public UserEntity get(Long id) throws NotFoundException {
        return userRepository.getOne(id);
    }

    @Transactional(readOnly = true)
    public UserEntity getByEmail(String email) throws NotFoundException {
        Objects.requireNonNull(email, "Email must not be empty");
        return userRepository.getByEmail(email);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public UserEntity update(UserEntity user) {
       return userRepository.saveAndFlush(user);
    }

    @Transactional(readOnly = true)
    public UserEntity getWithOrders(int id) {
        return userRepository.getByIdWithInitializedOrders(id);
    }
}


