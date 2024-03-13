package org.example.carpooling.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.example.carpooling.exceptions.*;
import org.example.carpooling.helpers.ValidationHelper;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.example.carpooling.services.contracts.UserSecurityCodeService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    private static final String ERROR_MESSAGE = "You are not authorized";
    public static final String ACTIVATING_USER_IS_NOT_PERMITTED = "Activating user is not permitted";

    private final UserRepository userRepository;
    private final UserSecurityCodeService userSecurityCodeService;
    private final Cloudinary cloudinary;

    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserSecurityCodeService userSecurityCodeService, Cloudinary cloudinary, MailService mailService) {
        this.userRepository = userRepository;
        this.userSecurityCodeService = userSecurityCodeService;
        this.cloudinary = cloudinary;
        this.mailService = mailService;
    }

    @Override
    public List<User> getAllUsers(UserFilterOptions filterOptions) {
        return userRepository.getAllUsers(filterOptions);
    }

    @Override
    public User getUserById(long id, User currentUser) {
        validateIsAdminOrOwner(id, currentUser);
        return getById(id);
    }

    private User getById(long id) {
        return userRepository.getByUserId(id);
    }


//    @Override
//    public User getByPhoneNumber(String phoneNumber, User user) {
//        checkAccessPermissionString(phoneNumber, user);
//        return userRepository.getByPhoneNumber(phoneNumber);
//    }
//
//    @Override
//    public User getByEmail(String email, User user) {
//        checkAccessPermissionString(email, user);
//        return userRepository.getByEmail(email);
//    }
//
//    @Override
//    public User getByUsername(String username, User user) {
//        checkAccessPermissionString(username, user);
//        return userRepository.getByUsername(username);
//    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User create(User user) throws SendMailException {
        validateUserInfo(user);
        user.setUserStatus(UserStatus.PENDING);
        userRepository.create(user);
        UserSecurityCode securityCode = userSecurityCodeService.create(user);
        mailService.sendConformationEmail(user, securityCode.getSecurityCode());
        return user;
    }


    @Override
    public User update(User updatedUser) {
        User existingUser = getById(updatedUser.getUserId());
        validateUserInfo(updatedUser);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        return userRepository.update(existingUser);
    }

    @Override
    public void delete(long userId, User currentUser) {
        //todo what to do with deleted uncompleted travels
        validateIsAdminOrOwner(userId, currentUser);
        User userToDelete = getById(userId);
        userToDelete.setUserStatus(UserStatus.DELETED);
        userRepository.update(userToDelete);
    }

    @Override
    public User makeUserAdmin(long id, User currentUser) {
        validateIsAdmin(currentUser);
        User userToUpdate = getById(id);
        if (userToUpdate.isAdmin()) {
            throw new EntityAttributeAlreadySetException("User", "admin", String.valueOf(userToUpdate.isAdmin()));
        }
        userToUpdate.setAdmin(true);
        return userRepository.update(userToUpdate);
    }

    @Override
    public User unmakeUserAdmin(long id, User currentUser) {
        validateIsAdmin(currentUser);
        User userToUpdate = getById(id);
        if (!userToUpdate.isAdmin()) {
            throw new EntityAttributeAlreadySetException("User", "admin", String.valueOf(userToUpdate.isAdmin()));
        }
        userToUpdate.setAdmin(false);
        return userRepository.update(userToUpdate);
    }

    @Override
    public User blockUser(long id, User currentUser) {
        //todo Pet: what to do with blocked travels
        validateIsAdmin(currentUser);
        User userToBlock = getById(id);
        if (UserStatus.BLOCKED == userToBlock.getUserStatus()) {
            throw new EntityAttributeAlreadySetException("User", "status", userToBlock.getUserStatus().name());
        }
        userToBlock.setUserStatus(UserStatus.BLOCKED);
        return userRepository.update(userToBlock);
    }

    @Override
    public User unblockUser(long id, User currentUser) {
        validateIsAdmin(currentUser);
        User userToUnblock = getById(id);
        if (UserStatus.BLOCKED != userToUnblock.getUserStatus()) {
            throw new EntityAttributeAlreadySetException("User", "status", userToUnblock.getUserStatus().name());
        }
        userToUnblock.setUserStatus(UserStatus.ACTIVE);
        return userRepository.update(userToUnblock);
    }

    private void validateIsAdminOrOwner(long id, User currentUser) {
        if (!currentUser.isAdmin() && currentUser.getUserId() != id) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void validateIsAdmin(User currentUser) {
        if (!currentUser.isAdmin()) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void validateIsOwner(long id, User currentUser) {
        if (currentUser.getUserId() != id) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void validateUserInfo(User user) {
        ValidationHelper.validatePassword(user.getPassword());
        validateUniqueUsername(user);
        validateUniqueEmail(user);
        validateUniquePhoneNumber(user);
    }

    //todo Pet: check username, email, phone number with one query
    private void validateUniqueUsername(User user) {
        boolean duplicateUserExists = true;
        try {
            User foundUser = userRepository.getByUsername(user.getUsername());
            if (foundUser.getUserId() == user.getUserId()) {
                duplicateUserExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateUserExists = false;
        }
        if (duplicateUserExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
    }

    private void validateUniqueEmail(User user) {
        boolean duplicateUserExists = true;
        try {
            User foundUser = userRepository.getByEmail(user.getEmail());
            if (foundUser.getUserId() == user.getUserId()) {
                duplicateUserExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateUserExists = false;
        }
        if (duplicateUserExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }
    }

    private void validateUniquePhoneNumber(User user) {
        boolean duplicateUserExists = true;
        try {
            User foundUser = userRepository.getByPhoneNumber(user.getPhoneNumber());
            if (foundUser.getUserId() == user.getUserId()) {
                duplicateUserExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateUserExists = false;
        }
        if (duplicateUserExists) {
            throw new EntityDuplicateException("User", "phone number", user.getPhoneNumber());
        }
    }

    @Override
    public ImageData saveImage(MultipartFile file, User user) throws IOException {
        //todo validate file content
        String url = uploadFile(file);
        ImageData imageData = new ImageData();
        imageData.setImage(url);
        imageData.setUser(user);
        return userRepository.saveImage(imageData);
    }

    @Override
    public void verify(long id, long securityCode) {
        User user = getById(id);
        UserSecurityCode userSecurityCode = userSecurityCodeService.getCodeByUserId(id);
        if (securityCode != userSecurityCode.getSecurityCode()) {
            throw new OperationNotAllowedException(ACTIVATING_USER_IS_NOT_PERMITTED);
        }
        user.setUserStatus(UserStatus.ACTIVE);
        userSecurityCodeService.delete(userSecurityCode);
        userRepository.update(user);
    }

    private String uploadFile(MultipartFile file) throws IOException {
        Map upload = cloudinary.uploader()
                .upload(file.getBytes()
                        , ObjectUtils.asMap("resource_type", "auto"));

        return (String) upload.get("url");
    }

    @Override
    public long getUserCount() {
        return userRepository.getUserCount();
    }
}
