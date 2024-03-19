package org.example.carpooling;

import org.example.carpooling.models.Travel;
import org.example.carpooling.models.User;
import org.example.carpooling.models.enums.TravelStatus;
import org.example.carpooling.models.enums.UserStatus;

import java.time.LocalDateTime;

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

    public static Travel createMockTravel() {
        var mockUser = createMockUser();
        var mockTravel = new Travel();
        mockTravel.setTravelId(1);
        mockTravel.setStartPoint("MockStartLocation");
        mockTravel.setEndPoint("MockEndLocation");
        mockTravel.setDepartureTime(LocalDateTime.parse("2023-07-30T18:00"));
        mockTravel.setFreeSpots(4);
        mockTravel.setDeleted(false);
        mockTravel.setUserId(mockUser);
        mockTravel.setTravelStatus(TravelStatus.AVAILABLE);
        mockTravel.setDistanceTravel(222);
        mockTravel.setDurationTravel(222);
        mockTravel.setTravelComment("MockTravelComment");
        return mockTravel;
    }

    
}
