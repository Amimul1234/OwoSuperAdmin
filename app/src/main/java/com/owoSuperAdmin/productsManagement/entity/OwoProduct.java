package com.owoSuperAdmin.productsManagement.entity;

import androidx.annotation.Nullable;
import com.owoSuperAdmin.categoryManagement.brand.addBrand.Brands;
import java.io.Serializable;

public class OwoProduct implements Serializable {
    private Long productId;
    private String productName;
    private Long productCategoryId;
    private Long productSubCategoryId;
    private Double productPrice;
    private Double productDiscount;
    private Integer productQuantity;
    private String productDescription;
    private String productCreationDate;
    private String productCreationTime;
    private String productImage;
    private Brands brands;

    public OwoProduct() {

    }

    public OwoProduct(long productId, String productName, Long productCategoryId,
                      Long productSubCategoryId, Double productPrice, Double productDiscount,
                      Integer productQuantity, String productDescription,
                      String productCreationDate, String productCreationTime,
                      String productImage, Brands brands)
    {
        this.productId = productId;
        this.productName = productName;
        this.productCategoryId = productCategoryId;
        this.productSubCategoryId = productSubCategoryId;
        this.productPrice = productPrice;
        this.productDiscount = productDiscount;
        this.productQuantity = productQuantity;
        this.productDescription = productDescription;
        this.productCreationDate = productCreationDate;
        this.productCreationTime = productCreationTime;
        this.productImage = productImage;
        this.brands = brands;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Long productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Long getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(Long productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double productPrice) {
        this.productPrice = productPrice;
    }

    public Double getProductDiscount() {
        return productDiscount;
    }

    public void setProductDiscount(Double productDiscount) {
        this.productDiscount = productDiscount;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCreationDate() {
        return productCreationDate;
    }

    public void setProductCreationDate(String productCreationDate) {
        this.productCreationDate = productCreationDate;
    }

    public String getProductCreationTime() {
        return productCreationTime;
    }

    public void setProductCreationTime(String productCreationTime) {
        this.productCreationTime = productCreationTime;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Brands getBrands() {
        return brands;
    }

    public void setBrands(Brands brands) {
        this.brands = brands;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        OwoProduct owoProduct = (OwoProduct) obj;
        return this.productId.equals(owoProduct.productId);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
