package org.example.carpooling.services;

import org.example.carpooling.exceptions.*;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String ERROR_MESSAGE = "You are not authorized";

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers(UserFilterOptions filterOptions) {
        return userRepository.getAllUsers(filterOptions);
    }

    @Override
    public User getById(int id) {
        return userRepository.getByUserId(id);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber, User user) {
        checkAccessPermissionString(phoneNumber, user);
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public User getByEmail(String email, User user) {
        checkAccessPermissionString(email, user);
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByUsername(String username, User user) {
        checkAccessPermissionString(username, user);
        return userRepository.getByUsername(username);
    }

    @Override
    public User getByUsernameAuthentication(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public User create(User user) {
        validateUniqueUsername(user);
        validateUniqueEmail(user);
        validateUniquePhoneNumber(user);
        user.setUserStatus(UserStatus.PENDING);
        return userRepository.create(user);
    }


    @Override
    public User update(User updatedUser) {
        User existingUser = getById(updatedUser.getUserId());
        validateUniqueUsername(updatedUser);
        validateUniqueEmail(updatedUser);
        validateUniquePhoneNumber(updatedUser);
        existingUser.setFirstName(updatedUser.getFirstName());
        existingUser.setLastName(updatedUser.getLastName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());
        return userRepository.update(existingUser);
    }

    @Override
    public User delete(int userId, User userModifier) {
        //todo what to do with deleted uncompleted travels

        validateAdminOrOwner(userId, userModifier);
        User userToDelete = getById(userId);
        userToDelete.setUserStatus(UserStatus.DELETED);
        return userRepository.delete(userToDelete);
    }

    //todo check @ for email
//    private void validateEmail(User user, User userToUpdate) {
//        if (user.getEmail().equals(userToUpdate.getEmail())) {
//            throw new EmailChangeProfileError(EMAIL_IS_THE_SAME_AS_BEFORE_ERROR);
//        }
//        if (user.getEmail().isEmpty()) {
//            userToUpdate.setEmail(userToUpdate.getEmail());
//        } else {
//            userToUpdate.setEmail(user.getEmail());
//        }
//    }
//    private void validateUsername(User user, User userToUpdate) {
////        if (user.getUsername().equals(userToUpdate.getUsername())) {
////            throw new EmailChangeProfileError(USERNAME_IS_THE_SAME_AS_BEFORE_ERROR);
////        }
//        if (user.getUsername().isEmpty()) {
//            userToUpdate.setUsername(userToUpdate.getUsername());
//        } else {
//            userToUpdate.setUsername(user.getUsername());
//        }
//    }

//    private void validatePhoneNumber(User user, User userToUpdate) {
//        if (user.getPhoneNumber().equals(userToUpdate.getPhoneNumber())) {
//            throw new EmailChangeProfileError(EMAIL_IS_THE_SAME_AS_BEFORE_ERROR);
//        }
//        if (user.getPhoneNumber().isEmpty()) {
//            userToUpdate.setPhoneNumber(userToUpdate.getPhoneNumber());
//        } else {
//            userToUpdate.setPhoneNumber(user.getPhoneNumber());
//        }
//    }

//    private void validateFirstName(User user, User userToUpdate) {
//        if (user.getFirstName().isEmpty()) {
//            //todo Pet: upper check should be the opposite -> !
//            userToUpdate.setFirstName(userToUpdate.getFirstName());
//        }
//        if (!(user.getFirstName().isEmpty()) && (user.getFirstName().length() > 4 || user.getFirstName().length() < 32)) {
//            userToUpdate.setFirstName(user.getFirstName());
//        }
//        if (!(user.getFirstName().isEmpty()) && (user.getFirstName().length() < 4 || user.getFirstName().length() > 32)) {
//            throw new FirstNameChangeProfileError(FIRST_NAME_MUST_BE_BETWEEN_2_AND_20_SYMBOLS_ERROR);
//        }
//        //todo Pet: can you simplify that method ?
//    }

//    private void validateLastName(User user, User userToUpdate) {
//        if (user.getLastName().isEmpty()) {
//            userToUpdate.setLastName(userToUpdate.getLastName());
//        }
//        if ((!user.getLastName().isEmpty()) && (user.getLastName().length() < 4 || user.getLastName().length() > 32)) {
//            throw new LastNameChangeProfileError(LAST_NAME_MUST_BE_BETWEEN_2_AND_20_SYMBOLS_ERROR);
//        }
//        if (!(user.getLastName().isEmpty()) && (user.getLastName().length() > 4 || user.getLastName().length() < 32)) {
//            userToUpdate.setLastName(user.getLastName());
//        }
//    }


    @Override
    public User makeUserAdmin(int id, User userModifier) {
        validateIsAdmin(userModifier);
        return userRepository.makeUserAdmin(id);
    }

    @Override
    public User unmakeUserAdmin(int id, User userModifier) {
        validateIsAdmin(userModifier);
        return userRepository.unmakeUserAdmin(id);
    }

    @Override
    public User blockUser(int id, User userModifier) {
        //todo what to do with blocked travels

        validateIsAdmin(userModifier);
        User userToBlock = getById(id);
        userToBlock.setUserStatus(UserStatus.BLOCKED);
        return userRepository.blockUser(userToBlock);
    }

    @Override
    public User unblockUser(int id, User userModifier) {
        validateIsAdmin(userModifier);
        User userToUnblock = getById(id);
        userToUnblock.setUserStatus(UserStatus.ACTIVE);
        return userRepository.unblockUser(userToUnblock);
    }

    private void validateAdminOrOwner(int id, User requestingUser) {
        if (!requestingUser.isAdmin() && requestingUser.getUserId() != id) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void validateIsAdmin(User requestingUser) {
        if (!requestingUser.isAdmin()) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void validateIsOwner(int id, User requestingUser) {
        if (requestingUser.getUserId() != id) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    private void checkAccessPermissionString(String input, User requestingUser) {
        if (!requestingUser.isAdmin()) {
//            if (!requestingUser.getUsername().equals(input)) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    //todo check username, email, phone number with one query
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
    public ImageData saveImage(ImageData imageData, User user) {
       return userRepository.saveImage(imageData);
    }

    @Override
    public long getUserCount() {
        return userRepository.getUserCount();
    }
}
