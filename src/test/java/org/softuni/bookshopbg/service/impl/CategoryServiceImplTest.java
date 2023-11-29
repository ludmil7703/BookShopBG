package org.softuni.bookshopbg.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.CategoryRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {


    @Mock
    private CategoryRepository mockCategoryRepository;
    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testGetAllCategories() {
        // Arrange
        Category category = new Category();
        category.setCategoryName(CategoryName.IT);
        when(mockCategoryRepository.findAll())
                .thenReturn(java.util.List.of(category));
        // Act
        List<Category> categories = mockCategoryRepository.findAll();
        // Assert
        assertEquals(1, categories.size());
        assertEquals(categories.get(0).getCategoryName(), category.getCategoryName());
    }

}