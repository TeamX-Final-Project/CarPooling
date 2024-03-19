package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.UserSecurityCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSecurityCodeRepository extends JpaRepository<UserSecurityCode, Long> {

    UserSecurityCode getByUserId(long userId);
}
