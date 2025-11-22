package com.foodallergy.app.user;

import org.springframework.data.repository.CrudRepository;
import com.foodallergy.app.user.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
}
