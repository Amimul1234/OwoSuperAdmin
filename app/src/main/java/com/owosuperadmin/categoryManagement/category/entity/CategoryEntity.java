package com.owosuperadmin.categoryManagement.category.entity;

import com.owosuperadmin.categoryManagement.subCategory.entity.SubCategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryEntity {

    private Long categoryId;
    private String categoryName;
    private String categoryImage;
    private List<SubCategoryEntity> subCategoryEntities = new ArrayList<>();

    public CategoryEntity() {
    }

    public CategoryEntity(String categoryName, String categoryImage, List<SubCategoryEntity> subCategoryEntities) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
        this.subCategoryEntities = subCategoryEntities;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public List<SubCategoryEntity> getSubCategoryEntities() {
        return subCategoryEntities;
    }

    public void setSubCategoryEntities(List<SubCategoryEntity> subCategoryEntities) {
        this.subCategoryEntities = subCategoryEntities;
    }
}
