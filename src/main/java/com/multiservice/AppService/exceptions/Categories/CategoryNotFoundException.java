package com.multiservice.AppService.exceptions.Categories;


public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(Long id) {
        super("Category not found with ID: " + id);
    }
}

