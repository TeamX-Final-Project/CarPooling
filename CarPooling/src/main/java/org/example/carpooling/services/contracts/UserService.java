package org.example.carpooling.services.contracts;


import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.dto.UserDto;
import org.example.carpooling.models.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {

    Page<User> getAllUsers(TravelFilterOptions filterOptions, User currentUser);

    User getUserById(long id, User currentUser);

    User getById(long id);

    User getByUsername(String username);

    User create(User user) throws SendMailException;

    User update(User user);

    void delete(long id, User userModifier);

    User changeUserAdminValue(long id, User currentUser, boolean isAdmin);

    User addProfilePhoto(User user, MultipartFile file) throws IOException;

    long getUserCount();

    User updateUserStatus(long id, User currentUser, UserStatus userStatus);

    void verify(long id, long securityCode);

    User updateUser(User user, User updatedUser, UserDto userDto);
}
