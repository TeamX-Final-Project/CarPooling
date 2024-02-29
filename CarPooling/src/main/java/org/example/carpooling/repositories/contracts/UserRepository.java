package org.example.carpooling.repositories.contracts;

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

//    User delete(int id);

    User makeUserAdmin(int id);

    User unmakeUserAdmin(int id);

//    User blockUser(int id);
//
//    User unblockUser(int id);
//
//    long getUserCount();
}

