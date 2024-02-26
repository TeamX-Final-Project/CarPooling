package org.example.carpooling.services;

import org.example.carpooling.exceptions.*;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final String ERROR_MESSAGE = "You are not authorized";
    public static final String LAST_NAME_MUST_BE_BETWEEN_2_AND_20_SYMBOLS_ERROR = "Last name must be between 2 and 20 symbols";
    public static final String EMAIL_IS_THE_SAME_AS_BEFORE_ERROR = "Email is the same as before";
    public static final String PASSWORD_IS_THE_SAME_AS_BEFORE_ERROR = "Password is the same as before";
    public static final String FIRST_NAME_MUST_BE_BETWEEN_2_AND_20_SYMBOLS_ERROR = "First name must be between 2 and 20 symbols";
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
    public User getByFirstName(String firstName, User user) {

        checkAccessPermissionString(firstName, user);
        return userRepository.getByFirstName(firstName);
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
        boolean duplicateUserExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateUserExists = false;
        }
        if (duplicateUserExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }
        return userRepository.create(user);
    }

    @Override
    public User update(User user) {
        checkAccessPermissionId(user.getUserId(), user);
        User userToUpdate = getById(user.getUserId());
        validateFirstName(user, userToUpdate);
        validateLastName(user, userToUpdate);
        validateEmail(user, userToUpdate);
        validatePassword(user, userToUpdate);

        //        TODO double check how to do the phone number field
//        userToUpdate.setPhoneNumber(user.getPhoneNumber);
        return userRepository.update(userToUpdate);
    }

    private static void validatePassword(User user, User userToUpdate) {
        if (user.getPassword().equals(userToUpdate.getPassword())) {
            throw new PasswordChangeProfileError(PASSWORD_IS_THE_SAME_AS_BEFORE_ERROR);
        }
        if (user.getPassword().isEmpty()) {
            userToUpdate.setPassword(userToUpdate.getPassword());
        } else {
            userToUpdate.setPassword(user.getPassword());
        }
    }

    private static void validateEmail(User user, User userToUpdate) {
        if (user.getEmail().equals(userToUpdate.getEmail())) {
            throw new EmailChangeProfileError(EMAIL_IS_THE_SAME_AS_BEFORE_ERROR);
        }
        if (user.getEmail().isEmpty()) {
            userToUpdate.setEmail(userToUpdate.getEmail());
        } else {
            userToUpdate.setEmail(user.getEmail());
        }
    }

    private static void validateFirstName(User user, User userToUpdate) {
        if (user.getFirstName().isEmpty()) {
            userToUpdate.setFirstName(userToUpdate.getFirstName());
        }
        if (!(user.getFirstName().isEmpty()) && (user.getFirstName().length() > 4 || user.getFirstName().length() < 32)) {
            userToUpdate.setFirstName(user.getFirstName());
        }
        if (!(user.getFirstName().isEmpty()) && (user.getFirstName().length() < 4 || user.getFirstName().length() > 32)) {
            throw new FirstNameChangeProfileError(FIRST_NAME_MUST_BE_BETWEEN_2_AND_20_SYMBOLS_ERROR);
        }
    }

    private static void validateLastName(User user, User userToUpdate) {
        if (user.getLastName().isEmpty()) {
            userToUpdate.setLastName(userToUpdate.getLastName());
        }
        if ((!user.getLastName().isEmpty()) && (user.getLastName().length() < 4 || user.getLastName().length() > 32)) {
            throw new LastNameChangeProfileError(LAST_NAME_MUST_BE_BETWEEN_2_AND_20_SYMBOLS_ERROR);
        }
        if (!(user.getLastName().isEmpty()) && (user.getLastName().length() > 4 || user.getLastName().length() < 32)) {
            userToUpdate.setLastName(user.getLastName());
        }
    }


    @Override
    public User delete(int id, User userModifier) {
        checkAccessPermissionId(id, userModifier);
        return userRepository.delete(id);
    }

    @Override
    public User makeUserAdmin(int id, User userModifier) {
        checkAccessPermissionId(id, userModifier);
        return userRepository.makeUserAdmin(id);
    }

    @Override
    public User unmakeUserAdmin(int id, User userModifier) {
        checkAccessPermissionId(id, userModifier);
        return userRepository.unmakeUserAdmin(id);
    }

    @Override
    public User blockUser(int id, User userModifier) {
        checkAccessPermissionId(id, userModifier);
        return userRepository.blockUser(id);
    }

    @Override
    public User unblockUser(int id, User userModifier) {
        checkAccessPermissionId(id, userModifier);
        return userRepository.unblockUser(id);
    }

    private void checkAccessPermissionId(int id, User requestingUser) {
        if (!requestingUser.isAdmin()) {
            if (requestingUser.getUserId() != id) {
                throw new AuthorizationException(ERROR_MESSAGE);
            }
        }
    }

    private void checkAccessPermissionString(String input, User requestingUser) {
        if (!requestingUser.isAdmin()) {
//            if (!requestingUser.getUsername().equals(input)) {
            throw new AuthorizationException(ERROR_MESSAGE);
        }
    }

    @Override
    public long getUserCount() {

        return userRepository.getUserCount();
    }
}
