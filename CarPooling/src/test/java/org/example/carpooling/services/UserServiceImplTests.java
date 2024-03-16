package org.example.carpooling.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import org.example.carpooling.Helpers;
import org.example.carpooling.exceptions.*;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.example.carpooling.services.contracts.MailService;
import org.example.carpooling.services.contracts.UserSecurityCodeService;
import org.example.carpooling.services.contracts.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

    @Mock
    UserRepository mockUserRepository;

    @Mock
    UserSecurityCodeService mockUserSecurityCodeService;

    @Mock
    MailService mockMailService;

    @Mock
    Cloudinary mockCloudinary;
    @Mock
    Uploader mockUploader;

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
        assertThrows(
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
        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class, () -> userService.create(userToCreate));
        assertTrue(exception.getMessage().contains("username"));
    }

    @Test
    public void create_should_return_user_already_exist_with_same_email() throws SendMailException {

        //Arrange
        User userToCreate = Helpers.createMockUser();
        userToCreate.setUserId(0);
        User dublicatedUser = Helpers.createMockUser();
        dublicatedUser.setEmail(userToCreate.getEmail());
        dublicatedUser.setUserId(3);

        Mockito.when(mockUserRepository.getByUsername(userToCreate.getUsername())).thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByEmail(userToCreate.getEmail())).thenReturn(dublicatedUser);

        //Act and Assert
        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class, () -> userService.create(userToCreate));
        assertTrue(exception.getMessage().contains("email"));
    }

    @Test
    public void create_should_return_user_already_exist_with_same_phoneNumber() throws SendMailException {

        //Arrange
        User userToCreate = Helpers.createMockUser();
        userToCreate.setUserId(0);
        User dublicatedUser = Helpers.createMockUser();
        dublicatedUser.setPhoneNumber(userToCreate.getPhoneNumber());
        dublicatedUser.setUserId(3);

        Mockito.when(mockUserRepository.getByUsername(userToCreate.getUsername())).thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByEmail(userToCreate.getEmail())).thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByPhoneNumber(userToCreate.getPhoneNumber())).thenReturn(dublicatedUser);

        //Act and Assert
        EntityDuplicateException exception = assertThrows(EntityDuplicateException.class, () -> userService.create(userToCreate));
        assertTrue(exception.getMessage().contains("phone number"));
    }

    @Test
    public void create_should_return_invalid_password_exception() {

        //Arrange
        User userToCreate = Helpers.createMockUser();
        userToCreate.setPassword("123wrong");

        //Act and Assert
        assertThrows(InvalidPasswordException.class, () -> userService.create(userToCreate));
    }

    @Test
    public void update_should_update_user_successfully() {

        //Arrange
        User userToUpdate = Helpers.createMockUser();
        userToUpdate.setUserId(0);
        userToUpdate.setEmail("email@1.com");
        User updatedUser = Helpers.createMockUser();
        updatedUser.setUserId(0);
        updatedUser.setEmail("email@1.com");
        User existingUser = Helpers.createMockUser();
        existingUser.setUserId(0);
        userToUpdate.setEmail("email@2.com");

        Mockito.when(mockUserRepository.getByUserId(userToUpdate.getUserId())).thenReturn(existingUser);
        Mockito.when(mockUserRepository.getByUsername(userToUpdate.getUsername())).
                thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByEmail(userToUpdate.getEmail())).
                thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.getByPhoneNumber(userToUpdate.getPhoneNumber())).
                thenThrow(new EntityNotFoundException(""));
        Mockito.when(mockUserRepository.update(userToUpdate)).thenReturn(updatedUser);

        //Act
        User result = userService.update(userToUpdate);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).update(userToUpdate);
    }

    @Test
    public void delete_should_throw_authorization_exception_when_user_is_not_admin_or_owner() {

        //Arrange
        User currentUser = Helpers.createMockUser();
        long userId = 4;

        //Act and Assert
        assertThrows(AuthorizationException.class, () -> userService.delete(userId, currentUser));
    }

    @Test
    public void delete_should_set_user_status_to_delete_when_user_is_admin() {

        //Arrange
        User currentUser = Helpers.createMockAdmin();
        User userToDelete = Helpers.createMockUser();
        long userId = userToDelete.getUserId();

        Mockito.when(mockUserRepository.getByUserId(userId)).thenReturn(userToDelete);


        //Act
        userService.delete(userId, currentUser);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).update(userToDelete);
    }

    @Test
    public void changeUserAdminValue_should_make_user_admin() {

        //Arrange
        User userToMakeAdmin = Helpers.createMockUser();
        User currentAdminUser = Helpers.createMockAdmin();
        long userId = userToMakeAdmin.getUserId();

        Mockito.when(mockUserRepository.getByUserId(userId)).thenReturn(userToMakeAdmin);

        //Act
        userService.changeUserAdminValue(userId,currentAdminUser, true);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).update(userToMakeAdmin);
    }

    @Test
    public void changeUserAdminValue_should_throw_authorization_exception_when_user_is_not_admin(){
        //Arrange
        User currentUser = Helpers.createMockUser();
        long userId = 5;

        //Act and Assert
        assertThrows(AuthorizationException.class,
                ()-> userService.changeUserAdminValue(userId,currentUser,false));
    }

    @Test
    public void changeUserAdminValue_should_throw_entity_attribute_already_set_exception(){
        //Arrange
        long userId = 5;
        User currentAdminUser = Helpers.createMockAdmin();
        User userToUpdate = Helpers.createMockAdmin();
        userToUpdate.setUserId(userId);

        Mockito.when(mockUserRepository.getByUserId(userId)).thenReturn(userToUpdate);

        //Act and Assert
        assertThrows(EntityAttributeAlreadySetException.class,
                ()->userService.changeUserAdminValue(userId,currentAdminUser,true));
    }

    @Test
    public void updateUserStatus_should_block_user() {

        //Arrange
        long userId = 5;
        User currentAdminUser = Helpers.createMockAdmin();
        User userToBlock = Helpers.createMockUser();
        userToBlock.setUserStatus(UserStatus.ACTIVE);
        userToBlock.setUserId(userId);

        Mockito.when(mockUserRepository.getByUserId(userId)).thenReturn(userToBlock);

        //Act
        userService.updateUserStatus(userId,currentAdminUser, UserStatus.BLOCKED);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).update(userToBlock);
    }

    @Test
    public void updateUserStatus_should_throw_entity_attribute_already_set_exception(){
        //Arrange
        long userId = 5;
        User currentAdminUser = Helpers.createMockAdmin();
        User userToBlock = Helpers.createMockUser();
        userToBlock.setUserStatus(UserStatus.BLOCKED);
        userToBlock.setUserId(userId);

        Mockito.when(mockUserRepository.getByUserId(userId)).thenReturn(userToBlock);

        //Act and Assert
        assertThrows(EntityAttributeAlreadySetException.class,
                ()->userService.updateUserStatus(userId,currentAdminUser,UserStatus.BLOCKED));
    }

    @Test
    public void saveImage_should_succeed() throws IOException {
        //Arrange
        MultipartFile file = new MockMultipartFile("name","".getBytes());
        User user = Helpers.createMockUser();
        Map uploadResponse = new HashMap<String, String>();

        Mockito.when(mockCloudinary.uploader()).thenReturn(mockUploader);
        Mockito.when(mockUploader.upload(Mockito.any(), Mockito.anyMap())).thenReturn(uploadResponse);

        //Act
        userService.saveImage(file,user);

        //Assertion
        Mockito.verify(mockUserRepository, Mockito.times(1)).saveImage(Mockito.any());
    }

    @Test
    public void verify_should_set_user_status_to_active(){
        //Arrange
        long userId = 5;
        long securityCode = 543453453;
        User user = Helpers.createMockUser();
        user.setUserId(userId);
        UserSecurityCode userSecurityCode = new UserSecurityCode();
        userSecurityCode.setSecurityCode(securityCode);


        Mockito.when(mockUserRepository.getByUserId(userId)).thenReturn(user);
        Mockito.when(mockUserSecurityCodeService.getCodeByUserId(userId)).thenReturn(userSecurityCode);

        //Act
        userService.verify(userId, securityCode);

        //Assert
        Mockito.verify(mockUserRepository, Mockito.times(1)).update(user);
    }
}
