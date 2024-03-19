package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;

import java.util.List;
@Deprecated
public interface UserRepositoryOld {

    List<User> getAllUsers(UserFilterOptions filterOptions);

//    User getByUserId(long UserId);
//
//    User getByPhoneNumber(String phoneNumber);
//
//    User getByEmail(String email);
//
//    User getByUsername(String username);
//
//    User create(User user);
//
//    User update(User user);
//
//
//    long getUserCount();
}

