package com.owoSuperAdmin.giftAndDeals;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.owoSuperAdmin.owoshop.R;

public class CreateNewGift extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView giftImage;
    private EditText giftCardDetails;

    private final int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_gift);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create new gift card");
        progressDialog.setMessage("Please wait while we are creating new gift card");

        giftImage = findViewById(R.id.gift_image);
        giftCardDetails = findViewById(R.id.gift_card_description);
        Button createGiftCard = findViewById(R.id.create_gift_card);

        createGiftCard.setOnClickListener(v -> validateInput());
    }

    private void validateInput() {
        if()

    }

}