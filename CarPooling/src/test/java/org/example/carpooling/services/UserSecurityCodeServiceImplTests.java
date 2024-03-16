package org.example.carpooling.services;

import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.repositories.contracts.UserSecurityCodeRepository;
import org.example.carpooling.services.contracts.UserSecurityCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Any;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
public class UserSecurityCodeServiceImplTests {

    @Mock
    UserSecurityCodeRepository mockUserSecurityCodeRepository;

    @InjectMocks
    UserSecurityCodeServiceImpl mockUserSecurityCodeService;

    @Test
    public void create_should_save_user_security_code_in_database() {
        //Arrange
        User user = Helpers.createMockUser();
        long userId = 5;
        user.setUserId(userId);
        int userSecurityCode = 33333333;
        UserSecurityCode userSecurityCode1 = mockUserSecurityCodeRepository.getCodeByUserId(userId);

        Mockito.when(mockUserSecurityCodeService.getCodeByUserId(userId)).thenReturn(userSecurityCode1);

        //Act
        UserSecurityCode result = mockUserSecurityCodeService.create(user);

        //Assert
        Mockito.verify(mockUserSecurityCodeRepository, Mockito.times(1)).save(userSecurityCode1);
    }
    @Test
    public void crete_should_throw_EntityNotFoundException(){
        //Arrange
        User user = Helpers.createMockUser();
        long userId = 5;
        User user2 = Helpers.createMockUser();
        long userId2 = 2;
        user.setUserId(userId);
        int userSecurityCode = 33333333;
        UserSecurityCode userSecurityCode1 = mockUserSecurityCodeRepository.getCodeByUserId(userId);
        UserSecurityCode userSecurityCode2 = mockUserSecurityCodeRepository.getCodeByUserId(userId2);


        Mockito.when(mockUserSecurityCodeRepository.getCodeByUserId(user.getUserId())).thenReturn(userSecurityCode2);


        assertThrows(EntityNotFoundException.class, () -> mockUserSecurityCodeService.create(user));
    }
}
