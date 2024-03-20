package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    Page<User> findAll( Pageable pageable);

    Page<User> findAllByPhoneNumberContainingOrEmailContainingOrUsernameContaining
            (String phoneNumber, String email, String username, Pageable pageable);


    User getByUserId(long UserId);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    User getByUsername(String username);

    int countAllUsersByUserStatus(UserStatus userStatus);



}

