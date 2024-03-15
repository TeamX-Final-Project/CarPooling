package org.example.carpooling;

import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.UserStatus;

public class Helpers {

    public static final long ADMIN_USER_ID = 1;
    public static final long USER_ID = 2;

    public static User createMockAdmin() {
        User user = new User();
        user.setUserId(ADMIN_USER_ID);
        setCommonUserData(user);
        user.setAdmin(true);
        return user;
    }

    public static User createMockUser() {
        User user = new User();
        user.setUserId(USER_ID);
        setCommonUserData(user);
        user.setAdmin(false);
        return user;
    }

    private static void setCommonUserData(User user) {
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setUsername("Vanka");
        user.setEmail("ivan@ivan.com");
        user.setPhoneNumber("0888888884");
        user.setPassword("Password10$");
//        user.setUserStatus(UserStatus.ACTIVE);
    }


}
