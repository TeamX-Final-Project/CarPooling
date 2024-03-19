package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    //todo
//    List<User> getAllUsers(UserFilterOptions filterOptions);

    User getByUserId(long UserId);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    User getByUsername(String username);

    int countAllUsersByUserStatus(UserStatus userStatus);

}

