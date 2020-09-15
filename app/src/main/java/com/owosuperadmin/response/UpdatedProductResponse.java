package com.owosuperadmin.response;

import com.owosuperadmin.model.Products;

public class UpdatedProductResponse {
    private boolean error;
    private String message;
    private Products product;

    public UpdatedProductResponse(boolean error, String message, Products product) {
        this.error = error;
        this.message = message;
        this.product = product;
    }

    public boolean isError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    public Products getProduct() {
        return product;
    }
}
