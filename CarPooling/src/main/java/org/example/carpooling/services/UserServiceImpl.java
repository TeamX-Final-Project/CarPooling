package org.example.carpooling.services;

import org.example.carpooling.exceptions.*;
import org.example.carpooling.helpers.ImageHelper;
import org.example.carpooling.helpers.ValidationHelper;
import org.example.carpooling.models.TravelFilterOptions;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserSecurityCode;
import org.example.carpooling.models.enums.UserStatus;
import org.example.carpooling.repositories.contracts.UserRepository;
import org.example.carpooling.repositories.contracts.UserRepositoryOld;
import org.example.carpooling.services.contracts.MailService;
import org.example.carpooling.services.contracts.UserSecurityCodeService;
import org.example.carpooling.services.contracts.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class UserServiceImpl implements UserService {
    private static final String ERROR_MESSAGE = "You are not authorized";
    public static final String ACTIVATING_USER_IS_NOT_PERMITTED = "Activating user is not permitted";

    private final UserRepositoryOld userRepositoryOld;
    private final UserRepository userRepository;
    private final UserSecurityCodeService userSecurityCodeService;
    private final ImageHelper imageHelper;
    private final MailService mailService;

    @Autowired
    public UserServiceImpl(UserRepositoryOld userRepositoryOld, UserRepository userRepository, UserSecurityCodeService userSecurityCodeService, ImageHelper imageHelper, MailService mailService) {
        this.userRepositoryOld = userRepositoryOld;
        this.userRepository = userRepository;
        this.userSecurityCodeService = userSecurityCodeService;
        this.imageHelper = imageHelper;
        this.mailService = mailService;
    }

    @Override
    public Page<User> getAllUsers(TravelFilterOptions filterOptions, User currentUser) {
        validateIsAdmin(currentUser);
        Pageable pageable = PageRequest.of(filterOptions.getPage(), filterOptions.getSize(),
                Sort.by(Sort.Direction.fromString(filterOptions.getOrderBy()), filterOptions.getSortBy()));
        if (filterOptions.getKeyword() != null && !filterOptions.getKeyword().isEmpty()) {
            return findAllByPhoneMailOrUsername(filterOptions.getKeyword(), pageable);
        } else {
            return findAll(pageable);
        }
    }

    private Page<User> findAllByPhoneMailOrUsername(String keyword, Pageable pageable) {
        return userRepository.findAllByPhoneNumberContainingOrEmailContainingOrUsernameContaining(keyword, keyword, keyword, pageable);
    }

    private Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }


    @Override
    public User getUserById(long id, User currentUser) {
        validateIsAdminOrOwner(id, currentUser);
        return getById(id);
    }

    @Override
    public User getById(long id) {
        User user = userRepository.getByUserId(id);
        if (user == null) {
            throw new EntityNotFoundException("User", id);
        }
        return user;
    }

    @Override
    public User getByUsername(String username) {
        User user = userRepository.getByUsername(username);
        if (user == null) {
            throw new EntityNotFoundException("User", "username", username);
        }
        return user;
    }

    @Override
    public User create(User user) throws SendMailException {
        validateUserInfo(user);
        user.setUserStatus(UserStatus.PENDING);
        User createdUser = userRepository.save(user);
        UserSecurityCode securityCode = userSecurityCodeService.create(user);
        mailService.sendConformationEmail(user, securityCode.getSecurityCode());
        return createdUser;
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
        return userRepository.save(existingUser);
    }

    @Override
    public void delete(long userId, User currentUser) {
        //todo what to do with deleted uncompleted travels
        validateIsAdminOrOwner(userId, currentUser);
        User userToDelete = getById(userId);
        userToDelete.setUserStatus(UserStatus.DELETED);
        userRepository.save(userToDelete);
    }

    @Override
    public User changeUserAdminValue(long id, User currentUser, boolean isAdmin) {
        validateIsAdmin(currentUser);
        User userToUpdate = getById(id);
        if (isAdmin == userToUpdate.isAdmin()) {
            throw new EntityAttributeAlreadySetException("User", "admin", String.valueOf(userToUpdate.isAdmin()));
        }
        userToUpdate.setAdmin(isAdmin);
        return userRepository.save(userToUpdate);
    }

    @Override
    public User updateUserStatus(long id, User currentUser, UserStatus userStatus) {
        //todo Pet: what to do with blocked travels
        validateIsAdmin(currentUser);
        User userToUpdate = getById(id);
        if (userStatus == userToUpdate.getUserStatus()) {
            throw new EntityAttributeAlreadySetException("User", "status", userToUpdate.getUserStatus().name());
        }
        userToUpdate.setUserStatus(userStatus);
        return userRepository.save(userToUpdate);
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
        userRepository.save(user);
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

    //todo Pet: delete if this method is unused
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
            User foundUser = getByUsername(user.getUsername());
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
            User foundUser = getByEmail(user.getEmail());
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
            User foundUser = getByPhoneNumber(user.getPhoneNumber());
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
    public User addProfilePhoto(User user, MultipartFile file) throws IOException {
        String url = imageHelper.uploadImage(file);
        user.setProfilePictureUrl(url);
        return userRepository.save(user);
    }

    @Override
    public long getUserCount() {
        return userRepository.countAllUsersByUserStatus(UserStatus.ACTIVE);
    }


    private User getByPhoneNumber(String phoneNumber) {
        User user = userRepository.getByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new EntityNotFoundException("User", "phoneNumber", phoneNumber);
        }
        return user;
    }


    private User getByEmail(String email) {
        User user = userRepository.getByEmail(email);
        if (user == null) {
            throw new EntityNotFoundException("User", "email", email);
        }
        return user;
    }
}
