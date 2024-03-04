package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers(UserFilterOptions filterOptions);

    User getByUserId(int UserId);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    User getByUsername(String username);

    User create(User user);

    User update(User user);

    User delete(User user);

    User makeUserAdmin(int id);

    User unmakeUserAdmin(int id);

    User blockUser(User user);

    User unblockUser(User user);

    ImageData saveImage(ImageData imageData);

    long getUserCount();
}

