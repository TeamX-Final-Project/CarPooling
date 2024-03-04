package org.example.carpooling.helpers;

import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.ProfileDto;
import org.example.carpooling.models.dto.RegisterDto;
import org.example.carpooling.models.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserMapper() {
    }

    public User fromDto(int id, UserDto userDto) {
        User user = fromDto(userDto);
        user.setUserId(id);
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

//    public User mapUpdates(UserDto userUpdates, User existingUser) {
//        existingUser.setUsername(userUpdates.getUsername());
//        existingUser.setPassword(userUpdates.getPassword());
//        existingUser.setFirstName(userUpdates.getFirstName());
//        existingUser.setLastName(userUpdates.getLastName());
//        existingUser.setEmail(userUpdates.getEmail());
//        existingUser.setPhoneNumber(userUpdates.getPhoneNumber());
//        return existingUser;
//    }

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        return user;
    }

    public User fromDto(ProfileDto profileDto, User user) {
        user.setFirstName(profileDto.getFirstName());
        user.setLastName(profileDto.getLastName());
        user.setEmail(profileDto.getEmail());
        user.setPassword(profileDto.getPassword());
        user.setPhoneNumber(profileDto.getPhoneNumber());
        return user;
    }
}