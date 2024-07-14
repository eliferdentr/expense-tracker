package com.eliferden.expensetracker.service;

import com.eliferden.expensetracker.model.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(Category category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    List<Category> getCategoriesByUser(Long userId);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id);
}
