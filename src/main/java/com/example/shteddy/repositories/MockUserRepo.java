package com.example.shteddy.repositories;

import com.example.shteddy.user.User;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MockUserRepo implements UserRepositories {

    private final Set<User> mockedUsers;

    public MockUserRepo() {
        User user1 = new User(1, "paulina", "mylove", "john.doe@example.com", new ArrayList<>());
        User user2 = new User(2, "jane_doe", "password", "jane.doe@example.com", new ArrayList<>());

        mockedUsers = new HashSet<>(Arrays.asList(user1, user2));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return mockedUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }
}
