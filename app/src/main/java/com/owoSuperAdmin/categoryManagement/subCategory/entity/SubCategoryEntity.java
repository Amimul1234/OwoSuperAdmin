package com.owoSuperAdmin.categoryManagement.subCategory.entity;

import java.io.Serializable;

public class SubCategoryEntity implements Serializable {
    private Long sub_category_id;
    private String sub_category_name;
    private String sub_category_image;

    public SubCategoryEntity() {
    }

    public SubCategoryEntity(String sub_category_name, String sub_category_image) {
        this.sub_category_name = sub_category_name;
        this.sub_category_image = sub_category_image;
    }

    public Long getSub_category_id() {
        return sub_category_id;
    }

    public String getSub_category_name() {
        return sub_category_name;
    }

    public void setSub_category_name(String sub_category_name) {
        this.sub_category_name = sub_category_name;
    }

    public String getSub_category_image() {
        return sub_category_image;
    }

    public void setSub_category_image(String sub_category_image) {
        this.sub_category_image = sub_category_image;
    }
}
