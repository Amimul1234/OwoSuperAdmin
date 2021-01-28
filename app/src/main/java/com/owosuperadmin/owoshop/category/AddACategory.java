package com.owosuperadmin.owoshop.category;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.owosuperadmin.owoshop.R;

public class AddACategory extends AppCompatActivity {

    private ImageView back_button;
    private ImageView category_image;
    private EditText category_name;
    private ProgressBar category_add_progressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_a_category);

        back_button = findViewById(R.id.back_button);
        category_image = findViewById(R.id.category_image);
        category_name = findViewById(R.id.category_name);
        category_add_progressbar = findViewById(R.id.category_add_progressbar);
    }
}