package org.example.carpooling.services;


import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.AuthorizationException;
import org.example.carpooling.exceptions.EntityDuplicateException;
import org.example.carpooling.exceptions.EntityNotFoundException;
import org.example.carpooling.exceptions.SendMailException;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.example.carpooling.services.contracts.UserSecurityCodeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    UserSecurityCodeService mockUserSecurityCodeService;

    @Mock
    MailService mockMailService;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    public void getUserById_should_return_user_when_call_by_admin() {
        //Arrange
        User user = Helpers.createMockAdmin();
        long id = Helpers.USER_ID;

        Mockito.when(mockUserRepository.getByUserId(id)).thenReturn(new User());

        //Act
        User result = userService.getUserById(id, user);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).getByUserId(id);
    }

    @Test
    public void getUserById_should_return_user_when_call_by_owner() {
        //Arrange
        User user = Helpers.createMockUser();
        long id = Helpers.USER_ID;

        Mockito.when(mockUserRepository.getByUserId(id)).thenReturn(new User());

        //Act
        User result = userService.getUserById(id, user);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).getByUserId(id);
    }

    @Test
    public void getUserById_should_throw_exception_when_is_called_from_neither_admin_or_owner() {

        //Arrange
        User user = Helpers.createMockUser();
        long id = 3;

        //Act and Assert
        Assertions.assertThrows(
                AuthorizationException.class,
                () -> userService.getUserById(id, user));
    }

    @Test
    public void create_should_create_user_successfully() throws SendMailException {

        //Arrange
        User userToCreate = Helpers.createMockUser();
        userToCreate.setUserId(0);
        User createdUser = Helpers.createMockUser();


        Mockito.when(mockUserRepository.getByUsername(userToCreate.getUsername())).
                thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByEmail(userToCreate.getEmail())).
                thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByPhoneNumber(userToCreate.getPhoneNumber())).
                thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.create(userToCreate)).thenReturn(createdUser);
        Mockito.when(mockUserSecurityCodeService.create(userToCreate)).thenReturn(new UserSecurityCode());
        Mockito.when(mockMailService.sendConformationEmail(Mockito.any(User.class), Mockito.anyLong())).thenReturn("");

        //Act
        User result = userService.create(userToCreate);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).create(userToCreate);

        Assertions.assertEquals(createdUser, result);
    }

    @Test
    public void create_should_return_user_already_exist_with_same_username() throws SendMailException {

        //Arrange
        User userToCreate = Helpers.createMockUser();
        userToCreate.setUserId(0);
        User dublicatedUser = Helpers.createMockUser();
        dublicatedUser.setUsername(userToCreate.getUsername());
        dublicatedUser.setUserId(3);

        Mockito.when(mockUserRepository.getByUsername(userToCreate.getUsername())).thenReturn(dublicatedUser);

        //Act and Assert
        Assertions.assertThrows(EntityDuplicateException.class, ()-> userService.create(userToCreate));
    }
}
