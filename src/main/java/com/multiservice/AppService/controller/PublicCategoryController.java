package com.multiservice.AppService.controller;

import com.multiservice.AppService.entities.Category;
import com.multiservice.AppService.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        log.info("Fetching all categories");
        List<Category> categories = categoryService.getAllCategories();
        log.info("Found {} categories", categories.size());
        return categories;
    }
} 