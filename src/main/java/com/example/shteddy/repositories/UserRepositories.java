package com.example.shteddy.repositories;

import com.example.shteddy.user.User;
import java.util.Optional;

public interface UserRepositories {
    Optional<User> findByUsername(String username);
}
