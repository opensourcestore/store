package opensource.onlinestore.model.util;

import opensource.onlinestore.model.enums.ActivityStatus;
import opensource.onlinestore.model.entity.UserEntity;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by malex on 26.01.16.
 */
public class UserEntityUtil {

    public static UserEntity createUser() {
        UserEntity entity = new UserEntity();
        entity.setUsername("Name_Us");
        entity.setPassword("123456789");
        entity.setFirstName("NameFirst");
        entity.setLastName("NameLast");
        entity.setAddress("Дом №2");
        entity.setRegistrationDate(new Date());
        entity.setEmail("www@www.com");
        entity.setActivityStatus(ActivityStatus.ACTIVE);
        return entity;
    }

    public static List<UserEntity> createUsers() {
        List<UserEntity> list = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            UserEntity entity = new UserEntity();
            entity.setId(1L + i);
            entity.setUsername("UserName" + i);
            entity.setPassword("123456789" + i);
            entity.setFirstName("NameFirst_" + i);
            entity.setLastName("NameLast_" + i);
            entity.setAddress("Дом №2_" + i);
            entity.setRegistrationDate(new Date());
            entity.setEmail("www_" + i + "@www.com");
            entity.setActivityStatus(ActivityStatus.ACTIVE);
            list.add(entity);
        }
        return list;
    }

}
