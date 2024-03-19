package org.example.carpooling.services.contracts;

//import org.example.carpooling.models.ImageData;

import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.models.enums.UserStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> getAllUsers(UserFilterOptions filterOptions);

    User getUserById(long id, User currentUser);

    User getById(long id);

    User getByUsername(String username);

    User create(User user) throws SendMailException;

    User update(User user);

    void delete(long id, User userModifier);

    User changeUserAdminValue(long id, User currentUser, boolean isAdmin);

    User addProfilePhoto(User user, String url);

    Double getAverageRatingForUser(User user);

    long getUserCount();

    User updateUserStatus(long id, User currentUser, UserStatus userStatus);



    void verify(long id, long securityCode);



}
