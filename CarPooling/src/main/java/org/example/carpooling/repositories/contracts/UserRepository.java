package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {


    Page<User> findAll( Pageable pageable);

    Page<User> findAllByPhoneNumberContainingOrEmailContainingOrUsernameContaining
            (String phoneNumber, String email, String username, Pageable pageable);


    User getByUserId(long UserId);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    User getByUsername(String username);

    int countAllUsersByUserStatus(UserStatus userStatus);

//@Query(nativeQuery = true,value = "select * from carpoolingx.feedbacks order by feedbacks.rating desc limit 10")
//List<User> top10ratingUsers();
}

