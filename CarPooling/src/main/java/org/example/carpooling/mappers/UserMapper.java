package org.example.carpooling.mappers;

import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.ProfileDto;
import org.example.carpooling.models.dto.RegisterDto;
import org.example.carpooling.models.dto.SimpleUserDto;
import org.example.carpooling.models.dto.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static final String ANONYMIZE = "Anonymize";

    public UserMapper() {
    }

    public User fromDto(UserDto userDto) {
        User user = new User();
        user.setUserId(userDto.getId());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setUsername(userDto.getUsername());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        user.setProfilePictureUrl("url");
        return user;
    }

    public UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setPassword(user.getPassword());
        return dto;
    }

    public UserDto toAnonymizedDto(User user) {
        UserDto dto = toDto(user);
        dto.setPassword(ANONYMIZE);
        dto.setPasswordConfirm(ANONYMIZE);
        return dto;
    }

    public SimpleUserDto toSimpleDto(User user) {
        SimpleUserDto dto = new SimpleUserDto();
        dto.setId(user.getUserId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setStatus(user.getUserStatus());
        dto.setAdmin(user.isAdmin());
        return dto;
    }

    public User fromDto(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setFirstName(registerDto.getFirstName());
        user.setLastName(registerDto.getLastName());
        user.setEmail(registerDto.getEmail());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        user.setProfilePictureUrl("url");
        return user;
    }



}