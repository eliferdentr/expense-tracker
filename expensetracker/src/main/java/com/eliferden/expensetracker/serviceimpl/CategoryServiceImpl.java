package com.eliferden.expensetracker.serviceimpl;

import com.eliferden.expensetracker.exception.CategoryNotFoundException;
import com.eliferden.expensetracker.exception.UserNotFoundException;
import com.eliferden.expensetracker.model.Category;
import com.eliferden.expensetracker.model.User;
import com.eliferden.expensetracker.repository.CategoryRepository;
import com.eliferden.expensetracker.repository.UserRepository;
import com.eliferden.expensetracker.service.CategoryService;
import com.eliferden.expensetracker.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }




    @Override
    public Category createCategory(Category category) {
        //  kategoriyi oluştururken veya güncellerken ilgili kullanıcının var olup olmadığını doğrula
        ValidationUtil.validateUserId(category.getUser().getId());
        User user = userRepository.findById(category.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + category.getUser().getId()));
        category.setUser(user);
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long id) {
        ValidationUtil.validateCategoryId(id);
        return categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Category> getCategoriesByUser(Long userId) {
        ValidationUtil.validateUserId(userId);
        return categoryRepository.findByUserId(userId);
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        ValidationUtil.validateCategoryId(id);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        existingCategory.setCategoryName(category.getCategoryName());
        return categoryRepository.save(existingCategory);
    }

    @Override
    public void deleteCategory(Long id) {
        ValidationUtil.validateCategoryId(id);
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found with id: " + id));
        categoryRepository.delete(existingCategory);
    }
}
