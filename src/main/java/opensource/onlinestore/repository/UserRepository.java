package opensource.onlinestore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import opensource.onlinestore.model.entity.UserEntity;

/**
 * Created by maks(avto12@i.ua) on 27.01.2016.
 */

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    UserEntity getByEmail(String email);

    @Query("SELECT u FROM UserEntity u JOIN FETCH u.orders WHERE u.id = ?1") /*оставил этот метод только для тестирования*/
    UserEntity getByIdWithInitializedOrders(Integer id);

}
