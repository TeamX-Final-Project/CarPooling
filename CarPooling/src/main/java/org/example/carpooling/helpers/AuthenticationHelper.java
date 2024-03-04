package org.example.carpooling.helpers;


import jakarta.servlet.http.HttpSession;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.LoginDto;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String INVALID_AUTHENTICATION_ERROR = "Invalid authentication";

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
        String username = getUsername(userInfo);
        String password = getPassword(userInfo);
        return verifyAuthentication(username, password);
    }

    public User tryAuthenticateUser(LoginDto loginDto) {
        try {
            User user = userService.getByUsernameAuthentication(loginDto.getUsername());
            if (!user.getPassword().equals(loginDto.getPassword())) {
                //TODO need to be authentication exception
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            //TODO need to be authentication exception
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    //todo Pet: this and next method look pretty similar;
    public User tryGetUserFromSession(HttpSession session) {
        try {
            return userService.getByUsernameAuthentication((String) session.getAttribute("currentUser"));
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }

    public User tryGetCurrentUser(HttpSession session) {
        String currentUsername = (String) session.getAttribute("currentUser");
        if (currentUsername == null) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userService.getByUsernameAuthentication(currentUsername);
    }

    public String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
        return userInfo.substring(firstSpace + 1);
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsernameAuthentication(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthorizationException(INVALID_AUTHENTICATION_ERROR);
        }
    }
}
