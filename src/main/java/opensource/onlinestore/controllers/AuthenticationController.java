package opensource.onlinestore.controllers;

import opensource.onlinestore.Utils.Exceptions.InvalidRequestException;
import opensource.onlinestore.model.authentication.UserAuthentication;
import opensource.onlinestore.model.dto.RegistrationResponseDTO;
import opensource.onlinestore.model.dto.UserDTO;
import opensource.onlinestore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

/**
 * Created by orbot on 29.01.16.
 */
@Controller
public class AuthenticationController {

    public static final Logger LOG = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    UserService userService;

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    @ResponseBody
    public UserDTO currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication instanceof UserAuthentication) {
            return ((UserAuthentication)authentication).getDetails().getUser();
        } else if(authentication == null) {
            return null;
        }
        UserDTO user = new UserDTO();
        user.setUsername(authentication.getName());
        return user;
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    @ResponseBody
    public RegistrationResponseDTO join(@Valid UserDTO user, BindingResult errors) {
        RegistrationResponseDTO response = new RegistrationResponseDTO();
        if(errors.hasErrors()) {
            throw new InvalidRequestException("Invalid register form", errors);
        }
        String token = userService.authenticateUserAndGetToken(
                userService.registerNewUser(user));
        response.setSuccess(true);
        response.setToken(token);
        return response;
    }
}
