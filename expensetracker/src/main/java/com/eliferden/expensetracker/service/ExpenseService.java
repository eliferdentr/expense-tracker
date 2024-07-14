package com.eliferden.expensetracker.service;

import com.eliferden.expensetracker.dto.ExpenseDTO;
import com.eliferden.expensetracker.model.Expense;

import java.util.List;

public interface ExpenseService {
    ExpenseDTO createExpense(ExpenseDTO expenseDTO);
    ExpenseDTO getExpenseById(Long id);
    List<ExpenseDTO> getAllExpenses();
    List<ExpenseDTO> getExpensesByUser(Long userId);
    List<ExpenseDTO> getExpensesByCategory(Long categoryId);
    ExpenseDTO updateExpense(Long id, ExpenseDTO expenseDTO);
    void deleteExpense(Long id);
}
