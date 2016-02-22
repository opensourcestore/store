package opensource.onlinestore.Utils;

import opensource.onlinestore.model.entity.UserEntity;

/**
 * Created by maks(avto12@i.ua) on 27.01.2016.
 */
public class UserUtils {

    public static UserEntity prepareToSave(UserEntity user) {
        user.setPassword(PasswordUtil.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
