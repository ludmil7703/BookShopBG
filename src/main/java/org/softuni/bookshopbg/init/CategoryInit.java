package org.softuni.bookshopbg.init;

import org.softuni.bookshopbg.model.entities.Category;
import org.softuni.bookshopbg.model.enums.CategoryName;
import org.softuni.bookshopbg.repositories.CategoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CategoryInit implements CommandLineRunner {
    private final CategoryRepository categoryRepository;

    public CategoryInit(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        boolean hasCategories = this.categoryRepository.count() > 0;

        if(!hasCategories){
            List<Category> categories = new ArrayList<>();
            Arrays.stream(CategoryName.values())
                    .forEach(categoryName -> {
                        Category category=new Category();
                        category.setCategoryName(categoryName);
                        categories.add(category);
                    });
            this.categoryRepository.saveAll(categories);
        }

    }
}
