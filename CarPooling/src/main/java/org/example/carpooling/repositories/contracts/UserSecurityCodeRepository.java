package org.example.carpooling.repositories.contracts;

import org.example.carpooling.models.UserSecurityCode;

public interface UserSecurityCodeRepository {
    UserSecurityCode save(UserSecurityCode securityCode);

    UserSecurityCode getCodeByUserId(int userId);

    void delete(UserSecurityCode userSecurityCode);
}