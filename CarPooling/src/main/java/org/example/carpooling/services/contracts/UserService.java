package org.example.carpooling.services.contracts;

import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;

import java.util.List;

public interface UserService {

    List<User> getAllUsers(UserFilterOptions filterOptions);

    User getById(int id);

    User getByFirstName(String firstName, User user);

    User getByEmail(String email, User user);

    User getByUsername(String username, User user);

    User getByUsernameAuthentication(String username);

    User create(User user);

    User update(User user);

    User delete(int id, User userModifier);

    User makeUserAdmin(int id, User userModifier);

    User unmakeUserAdmin(int id, User userModifier);

    User blockUser(int id, User userModifier);

    User unblockUser(int id, User userModifier);

    long getUserCount();
}
