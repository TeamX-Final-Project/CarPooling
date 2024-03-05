package org.example.carpooling.services.contracts;

//import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.ImageData;
import org.example.carpooling.models.User;
import org.example.carpooling.models.UserFilterOptions;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    List<User> getAllUsers(UserFilterOptions filterOptions);


    User getUserById(int id, User currentUser);

    User getByUsername(String username);

    User create(User user);

    User update(User user);

    void delete(int id, User userModifier);

    User makeUserAdmin(int id, User userModifier);

    User unmakeUserAdmin(int id, User userModifier);

    User blockUser(int id, User userModifier);

    User unblockUser(int id, User userModifier);

    long getUserCount();

   ImageData saveImage (MultipartFile file, User user) throws IOException;

}
