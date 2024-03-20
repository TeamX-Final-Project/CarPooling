package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers(UserFilterOptions filterOptions);

    User getByUserId(long UserId);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    User getByUsername(String username);

    User create(User user);

    User update(User user);


    User addProfilePhoto(User user);

    long getUserCount();
}

