package com.eliferden.expensetracker.serviceimpl;

import com.eliferden.expensetracker.dto.ExpenseDTO;
import com.eliferden.expensetracker.exception.CategoryNotFoundException;
import com.eliferden.expensetracker.exception.ExpenseNotFoundException;
import com.eliferden.expensetracker.exception.UserNotFoundException;
import com.eliferden.expensetracker.model.Category;
import com.eliferden.expensetracker.model.Expense;
import com.eliferden.expensetracker.model.User;
import com.eliferden.expensetracker.repository.CategoryRepository;
import com.eliferden.expensetracker.repository.ExpenseRepository;
import com.eliferden.expensetracker.repository.UserRepository;
import com.eliferden.expensetracker.service.ExpenseService;
import com.eliferden.expensetracker.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ExpenseDTO createExpense(ExpenseDTO expenseDTO) {
        // Kullanıcı ve Kategori ID'lerinin geçerli olup olmadığını kontrol et
        ValidationUtil.validateUserId(expenseDTO.getUserId());
        ValidationUtil.validateCategoryId(expenseDTO.getCategoryId());

        User user = userRepository.findById(expenseDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + expenseDTO.getUserId()));
        Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + expenseDTO.getCategoryId()));

        Expense expense = new Expense();
        expense.setAmount(expenseDTO.getAmount());
        expense.setDescription(expenseDTO.getDescription());
        expense.setDate(expenseDTO.getDate());
        expense.setUser(user);
        expense.setCategory(category);

        Expense savedExpense = expenseRepository.save(expense);
        return mapToDTO(savedExpense);
    }

    @Override
    public ExpenseDTO getExpenseById(Long id) {
        ValidationUtil.validateExpenseId(id);
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));
        return mapToDTO(expense);
    }

    @Override
    public List<ExpenseDTO> getAllExpenses() {
        return expenseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByUser(Long userId) {
        ValidationUtil.validateUserId(userId);
        return expenseRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ExpenseDTO> getExpensesByCategory(Long categoryId) {
        ValidationUtil.validateCategoryId(categoryId);
        return expenseRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO) {
        ValidationUtil.validateExpenseId(id);
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));

        existingExpense.setAmount(expenseDTO.getAmount());
        existingExpense.setDescription(expenseDTO.getDescription());
        existingExpense.setDate(expenseDTO.getDate());

        // Kategori ve kullanıcı değişebilir mi kontrolü
        if (!existingExpense.getCategory().getId().equals(expenseDTO.getCategoryId())) {
            Category category = categoryRepository.findById(expenseDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + expenseDTO.getCategoryId()));
            existingExpense.setCategory(category);
        }

        Expense updatedExpense = expenseRepository.save(existingExpense);
        return mapToDTO(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        ValidationUtil.validateExpenseId(id);
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense not found with id: " + id));
        expenseRepository.delete(existingExpense);
    }

    private ExpenseDTO mapToDTO(Expense expense) {
        return new ExpenseDTO(
                expense.getId(),
                expense.getAmount(),
                expense.getCategory().getId(),
                expense.getCategory().getCategoryName(),
                expense.getDescription(),
                expense.getDate(),
                expense.getUser().getId(),
                expense.getUser().getUsername()
        );
    }
}
