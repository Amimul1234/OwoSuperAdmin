package com.owoSuperAdmin.categoryManagement.category;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.owoshop.R;

public class UpdateCategoryActivity extends AppCompatActivity {

    private ImageView backButton;
    private ImageView categoryImage;
    private

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_category);

        CategoryEntity categoryEntity = (CategoryEntity) getIntent().getSerializableExtra("categoryEntity");


    }
}