package org.example.carpooling.services.contracts;

import org.example.carpooling.models.User;
import org.example.carpooling.models.UserSecurityCode;

public interface UserSecurityCodeService {
    UserSecurityCode getCodeByUserId(long userId);

    void delete(UserSecurityCode userSecurityCode);

    UserSecurityCode create(User user);
}
