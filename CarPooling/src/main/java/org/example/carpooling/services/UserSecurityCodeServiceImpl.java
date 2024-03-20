package org.example.carpooling.services;

import org.example.carpooling.models.User;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.repositories.contracts.UserSecurityCodeRepository;
import org.example.carpooling.services.contracts.UserSecurityCodeService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UserSecurityCodeServiceImpl implements UserSecurityCodeService {
    private final UserSecurityCodeRepository userSecurityCodeRepository;


    public UserSecurityCodeServiceImpl(UserSecurityCodeRepository userSecurityCodeRepository) {
        this.userSecurityCodeRepository = userSecurityCodeRepository;
    }

    @Override
    public UserSecurityCode getCodeByUserId(long userId) {
        return userSecurityCodeRepository.getByUserId(userId);
    }

    @Override
    public void delete(UserSecurityCode userSecurityCode) {
        userSecurityCodeRepository.delete(userSecurityCode);
    }

    @Override
    public UserSecurityCode create(User user) {
        UserSecurityCode userSecurityCode = new UserSecurityCode();
        userSecurityCode.setUserId(user.getUserId());
        userSecurityCode.setSecurityCode(new Random().nextInt());
        return userSecurityCodeRepository.save(userSecurityCode);
    }
}
