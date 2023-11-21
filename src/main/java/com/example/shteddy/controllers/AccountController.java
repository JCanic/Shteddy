package com.example.shteddy.controllers;

import com.example.shteddy.account.Account;
import com.example.shteddy.category.Category;
import com.example.shteddy.repositories.*;
import com.example.shteddy.service.AccountService;
import com.example.shteddy.transaction.Transaction;
import com.example.shteddy.transaction.TransactionDTO;
import com.example.shteddy.user.User;
import com.example.shteddy.user.UserLoginResponse;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5173")
@RequestMapping("/api")
public class AccountController {


    private final AccountsRepositories accountsRepositories;

    private final AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository; // Assuming you have a repository for categories


    public AccountController(AccountsRepositories accountsRepositories, AccountService accountService) {
        this.accountsRepositories = accountsRepositories;
        this.accountService = accountService;
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(List.copyOf(accountsRepositories.findAll()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsByUser(@PathVariable Integer userId) {
        List<Account> accounts = accountsRepositories.findByUserId(userId);
        if (accounts.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginUser) {
        Optional<User> optionalUser = userRepository.findByUsername(loginUser.getUsername());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(loginUser.getPassword())) {
                // Find the account associated with the user
                List<Account> accounts = accountsRepositories.findByUser(user);
                Integer accountId = accounts.isEmpty() ? null : accounts.get(0).getAccountID();


                // Include accountId in the response
                UserLoginResponse response = new UserLoginResponse(user.getId(), user.getUsername(), accountId);
                return ResponseEntity.ok(response);
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    @GetMapping("/user/{userId}/totalBalance")
    public ResponseEntity<BigDecimal> getTotalBalance(@PathVariable Integer userId) {
        BigDecimal totalBalance = accountService.getTotalBalanceByUserId(userId);
        return ResponseEntity.ok(totalBalance);
    }

    /*
    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<Transaction>> getTransactionsByUser(@PathVariable Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<Account> accounts = user.getAccounts();

        List<Transaction> allTransactions = new ArrayList<>();
        for (Account account : accounts) {
            List<Transaction> transactions = transactionRepository.findByAccount_AccountID(account.getAccountID());
            allTransactions.addAll(transactions);
        }

        return ResponseEntity.ok(allTransactions);
    }

     */

    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<List<TransactionDTO>> getTransactionsByUser(
            @PathVariable Long userId,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Long categoryId
    ) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (!userOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOptional.get();
        List<Account> accounts = user.getAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        Long accountId = accounts.get(0).getAccountID().longValue();
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (month != null) {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
            startDate = yearMonth.atDay(1);
            endDate = yearMonth.atEndOfMonth();
        }

        List<Transaction> transactions;
        if (startDate != null && endDate != null) {
            transactions = transactionRepository.findByAccountAndCategoryAndDateRange(accountId, startDate, endDate);
        } else {
            transactions = transactionRepository.findByAccount_AccountID(accountId.intValue());
        }

        // Convert to DTO
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(t -> new TransactionDTO(
                        t.getTransactionID(),
                        t.getDate(),
                        t.getAmount(),
                        t.getTransactionType(),
                        t.getDescription(),
                        t.getCategory() != null ? t.getCategory().getName() : "Unknown"
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(transactionDTOs);
    }


    @Transactional
    @PostMapping("/transactions")
    public ResponseEntity<Transaction> createTransaction(
            @RequestBody Transaction transaction,
            @RequestParam Integer accountId,
            @RequestParam Long categoryId) {

        Account account = accountsRepositories.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        transaction.setAccount(account);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        transaction.setCategory(category);

        if ("debit".equalsIgnoreCase(transaction.getTransactionType())) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else if ("credit".equalsIgnoreCase(transaction.getTransactionType())) {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        Transaction savedTransaction = transactionRepository.save(transaction);

        TransactionDTO transactionDTO = new TransactionDTO(
                savedTransaction.getTransactionID(),
                savedTransaction.getDate(),
                savedTransaction.getAmount(),
                savedTransaction.getTransactionType(),
                savedTransaction.getDescription(),
                savedTransaction.getCategory().getName()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
    }



    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/import_transactions")
    public ResponseEntity<?> importTransactions(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell dateCell = row.getCell(0);
                if(dateCell == null || dateCell.getCellType() == CellType.BLANK) {
                    // Skip this row or handle as needed if the date cell is empty
                    continue;
                }

                LocalDate date;
                if(dateCell.getCellType() == CellType.NUMERIC) {
                    date = dateCell.getDateCellValue().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                } else {
                    String dateStr = dateCell.getStringCellValue();
                    if(dateStr != null && !dateStr.trim().isEmpty()) {
                        date = LocalDate.parse(dateStr.trim(), formatter);
                    } else {
                        continue;
                    }
                }

                BigDecimal amount = BigDecimal.ZERO;
                if(row.getCell(3) != null && row.getCell(3).getCellType() == CellType.NUMERIC) {
                    amount = new BigDecimal(row.getCell(3).getNumericCellValue());
                } else if(row.getCell(3) != null && row.getCell(3).getCellType() == CellType.STRING) {
                    String amountStr = row.getCell(3).getStringCellValue();
                    if(amountStr != null && !amountStr.isEmpty()) {
                        amountStr = amountStr.replace(",", ".");
                        try {
                            amount = new BigDecimal(amountStr);
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }

                String description = row.getCell(7).getStringCellValue();

                //String category = categorizeTransaction(description);

                Transaction transaction = new Transaction();
                transaction.setDate(date);
                transaction.setAmount(amount);
                transaction.setDescription(description);
                Account account = accountsRepositories.findById(1)
                        .orElseThrow(() -> new RuntimeException("Account not found"));
                transaction.setAccount(account);

                Category category = categoryRepository.findById(1L).
                        orElseThrow(() -> new RuntimeException("Category not found"));
                //transaction.setCategory(category);
                transaction.setCategory(category);
                transaction.setTransactionType("debit");
                transactionRepository.save(transaction);
            }

            return ResponseEntity.ok("Transactions imported successfully");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to import transactions");
        }
    }




}
