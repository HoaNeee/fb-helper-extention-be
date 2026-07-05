package com.hoane.fbhelper.fbhelperextentionbe.reporitory;

import com.hoane.fbhelper.fbhelperextentionbe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByUsername(String username);

    Boolean existsUserByUsername(String username);
}
