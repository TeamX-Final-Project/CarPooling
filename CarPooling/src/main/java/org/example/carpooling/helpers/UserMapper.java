package org.example.carpooling.helpers;

import org.example.carpooling.models.dto.ProfileDto;
import org.example.carpooling.models.dto.RegisterDto;
import org.example.carpooling.models.dto.UserDto;
import org.example.carpooling.models.User;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final UserService userService;

    private final AuthenticationHelper authenticationHelper;

    public UserMapper(UserService userService, AuthenticationHelper authenticationHelper) {

        this.userService = userService;

        this.authenticationHelper = authenticationHelper;
    }

    public User fromDto(int id, UserDto userDto) {
        User user = fromDto(userDto);
        user.setUserId(id);
        //todo Pet: here should be only mapping logic; you shouldn't get user from service by id in the Mapper layer
        User repositoryUser = userService.getById(id);
        return user;
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        return user;
    }

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        return user;
    }

    public User fromDto(ProfileDto profileDto, User user) {
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setEmail(profileDto.getEmail());
        user.setPassword(profileDto.getPassword());

//        TODO double check how to do the phone number field
//        user.setPhoneNumber(profileDto.getPhoneNumber());
        return user;
    }

}