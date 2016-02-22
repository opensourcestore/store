package opensource.onlinestore.model.util;

import opensource.onlinestore.model.entity.AccountEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malex on 01.02.16.
 */
public class AccountEntityUtil {

    public static AccountEntity createAccountEntity(){
        AccountEntity entity = new AccountEntity();
        entity.setName("AccountUser");
        entity.setAmount(2.33);
        entity.setUser(null);
        return entity;
    }

    public static List<AccountEntity> createAccountEntityList(){
        List<AccountEntity> list =  new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AccountEntity entity = new AccountEntity();
            entity.setName("AccountUser_"+i);
            entity.setAmount(0.33+i);
            entity.setUser(null);
            list.add(entity);
        }
        return list;
    }
}
