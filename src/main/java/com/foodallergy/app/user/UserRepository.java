package com.foodallergy.app.user;

import com.foodallergy.app.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByUsername(String username);
    UserEntity findById(int id);
}
