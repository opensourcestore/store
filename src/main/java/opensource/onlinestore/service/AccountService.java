package opensource.onlinestore.service;

import opensource.onlinestore.model.entity.AccountEntity;

import java.util.List;

/**
 * Created by malex on 31.01.16.
 */
public interface AccountService {

    AccountEntity save(AccountEntity entity);

    AccountEntity update(AccountEntity entity);

    void delete(Long id);

    AccountEntity get(Long id);

    List<AccountEntity> getAll();

}
