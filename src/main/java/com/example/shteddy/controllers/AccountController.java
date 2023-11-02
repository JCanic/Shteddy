package com.example.shteddy.controllers;

import com.example.shteddy.account.Account;
import com.example.shteddy.repositories.AccountsRepositories;
import com.example.shteddy.repositories.UserRepositories;
import com.example.shteddy.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private final AccountsRepositories accountsRepositories;

    @Autowired
    private UserRepositories userRepository;

    public AccountController(AccountsRepositories accountsRepositories) {
        this.accountsRepositories = accountsRepositories;
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(List.copyOf(accountsRepositories.findAll()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable Integer userId) {
        List<Account> accounts = accountsRepositories.findByUser(userId);
        if(accounts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountsRepositories.save(account).orElseThrow());
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<Account> updateAccount(@PathVariable Integer accountId, @RequestBody Account updatedAccount) {
        Optional<Account> account = accountsRepositories.update(accountId, updatedAccount);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<Void> deleteAccountsByUser(@PathVariable Integer userId) {
        accountsRepositories.deleteByUser(userId);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "http://127.0.0.1:5173")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> optionalUser = userRepository.findByUsername(loginUser.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(loginUser.getPassword())) {  // In a real app, don't store plaintext passwords!
                return ResponseEntity.ok(user);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
