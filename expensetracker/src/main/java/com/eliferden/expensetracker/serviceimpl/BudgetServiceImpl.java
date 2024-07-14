package com.eliferden.expensetracker.serviceimpl;

import com.eliferden.expensetracker.dto.BudgetDTO;
import com.eliferden.expensetracker.exception.BudgetNotFoundException;
import com.eliferden.expensetracker.exception.CategoryNotFoundException;
import com.eliferden.expensetracker.exception.UserNotFoundException;
import com.eliferden.expensetracker.model.Budget;
import com.eliferden.expensetracker.model.Category;
import com.eliferden.expensetracker.model.User;
import com.eliferden.expensetracker.repository.BudgetRepository;
import com.eliferden.expensetracker.repository.CategoryRepository;
import com.eliferden.expensetracker.repository.UserRepository;
import com.eliferden.expensetracker.service.BudgetService;
import com.eliferden.expensetracker.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {
    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public BudgetDTO createBudget(BudgetDTO budgetDTO) {
        // Kullanıcı ve Kategori ID'lerinin geçerli olup olmadığını kontrol et
        ValidationUtil.validateUserId(budgetDTO.getUserId());
        ValidationUtil.validateCategoryId(budgetDTO.getCategoryId());

        User user = userRepository.findById(budgetDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + budgetDTO.getUserId()));
        Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + budgetDTO.getCategoryId()));

        Budget budget = new Budget();
        budget.setAmount(budgetDTO.getAmount());
        budget.setStartDate(budgetDTO.getStartDate());
        budget.setEndDate(budgetDTO.getEndDate());
        budget.setUser(user);
        budget.setCategory(category);

        Budget savedBudget = budgetRepository.save(budget);
        return mapToDTO(savedBudget);
    }

    @Override
    public BudgetDTO getBudgetById(Long id) {
        ValidationUtil.validateBudgetId(id);
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found with id: " + id));
        return mapToDTO(budget);
    }

    @Override
    public List<BudgetDTO> getAllBudgets() {
        return budgetRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BudgetDTO> getBudgetsByUser(Long userId) {
        ValidationUtil.validateUserId(userId);
        return budgetRepository.findByUserId(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BudgetDTO updateBudget(Long id, BudgetDTO budgetDTO) {
        ValidationUtil.validateBudgetId(id);
        Budget existingBudget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found with id: " + id));

        existingBudget.setAmount(budgetDTO.getAmount());
        existingBudget.setStartDate(budgetDTO.getStartDate());
        existingBudget.setEndDate(budgetDTO.getEndDate());

        // Kategori ve kullanıcı değişebilir mi kontrolü
        if (!existingBudget.getCategory().getId().equals(budgetDTO.getCategoryId())) {
            Category category = categoryRepository.findById(budgetDTO.getCategoryId())
                    .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + budgetDTO.getCategoryId()));
            existingBudget.setCategory(category);
        }

        Budget updatedBudget = budgetRepository.save(existingBudget);
        return mapToDTO(updatedBudget);
    }

    @Override
    public void deleteBudget(Long id) {
        ValidationUtil.validateBudgetId(id);
        Budget existingBudget = budgetRepository.findById(id)
                .orElseThrow(() -> new BudgetNotFoundException("Budget not found with id: " + id));
        budgetRepository.delete(existingBudget);
    }

    private BudgetDTO mapToDTO(Budget budget) {
        return new BudgetDTO(
                budget.getId(),
                budget.getAmount(),
                budget.getCategory().getId(),
                budget.getCategory().getCategoryName(),
                budget.getStartDate(),
                budget.getEndDate(),
                budget.getUser().getId(),
                budget.getUser().getUsername()
        );
    }
}
