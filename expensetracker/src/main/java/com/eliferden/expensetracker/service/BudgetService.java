package com.eliferden.expensetracker.service;

import com.eliferden.expensetracker.dto.BudgetDTO;

import java.util.List;

public interface BudgetService {
    BudgetDTO createBudget(BudgetDTO budgetDTO);
    BudgetDTO getBudgetById(Long id);
    List<BudgetDTO> getAllBudgets();
    List<BudgetDTO> getBudgetsByUser(Long userId);
    BudgetDTO updateBudget(Long id, BudgetDTO budgetDTO);
    void deleteBudget(Long id);
}
