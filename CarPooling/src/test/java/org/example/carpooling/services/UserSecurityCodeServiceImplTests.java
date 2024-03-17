package org.example.carpooling.services;

import org.example.carpooling.Helpers;
import org.example.carpooling.models.User;
import org.example.carpooling.repositories.contracts.UserSecurityCodeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class UserSecurityCodeServiceImplTests {

    @Mock
    UserSecurityCodeRepository mockUserSecurityCodeRepository;

    @InjectMocks
    UserSecurityCodeServiceImpl userSecurityCodeService;

    @Test
    public void create_should_save_user_security_code_in_database() {
        //Arrange
        User user = Helpers.createMockUser();

        //Act
        userSecurityCodeService.create(user);

        //Assert
        Mockito.verify(mockUserSecurityCodeRepository, Mockito.times(1)).save(Mockito.any());
    }
}
