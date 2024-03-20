package org.example.carpooling.mappers;

import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.ProfileDto;
import org.example.carpooling.models.dto.RegisterDto;
import org.example.carpooling.models.dto.SimpleUserDto;
import org.example.carpooling.models.dto.UserDto;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public static final String ANONYMIZE = "Anonymize";
    private final UserService userService;

    public UserMapper(UserService userService) {
        this.userService = userService;
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
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
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
        return user;
    }

public UserDto toDtoEdit(User user){
        UserDto userDto = new UserDto();
    userDto.setFirstName(user.getFirstName());
    userDto.setLastName(user.getLastName());
    userDto.setEmail(user.getEmail());
    userDto.setPhoneNumber(user.getPhoneNumber());
    return userDto;
}
public User fromEditUserDto(Long id,UserDto userDto){
      User user = new User();
    User existing = userService.getById(id);
    user.setUserId(id);
    user.setUsername(existing.getUsername());
    user.setPassword(existing.getPassword());
    user.setFirstName(userDto.getFirstName());
    user.setLastName(userDto.getLastName());
    user.setEmail(userDto.getEmail());
    user.setPhoneNumber(userDto.getPhoneNumber());
    user.setFeedbackList(existing.getFeedbackList());
    user.setAdmin(existing.isAdmin());
    return user;
}
}