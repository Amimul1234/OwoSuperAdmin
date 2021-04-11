package com.owoSuperAdmin.categoryManagement.brand.addBrand;

import com.owoSuperAdmin.categoryManagement.subCategory.entity.SubCategoryEntity;

import java.io.Serializable;

public class Brands implements Serializable {
    private Long brandId;
    private String brandName;
    private String brandImage;
    private SubCategoryEntity subCategoryEntity;


    public Brands() {
    }

    public Brands(String brandName, String brandImage, SubCategoryEntity subCategoryEntity) {
        this.brandName = brandName;
        this.brandImage = brandImage;
        this.subCategoryEntity = subCategoryEntity;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandImage() {
        return brandImage;
    }

    public void setBrandImage(String brandImage) {
        this.brandImage = brandImage;
    }

    public SubCategoryEntity getSubCategoryEntity() {
        return subCategoryEntity;
    }

    public void setSubCategoryEntity(SubCategoryEntity subCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity;
    }
}
