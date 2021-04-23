package com.owoSuperAdmin.giftAndDeals.deals;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.owoSuperAdmin.giftAndDeals.entity.Deals;
import com.owoSuperAdmin.giftAndDeals.entity.Gifts;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateDeal extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ImageView dealImage;
    private EditText dealDetails;

    private final int STORAGE_PERMISSION_CODE = 1;
    private final String TAG = "Create Deal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_deal);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Create new deal");
        progressDialog.setMessage("Please wait while we are creating new deal");

        dealImage = findViewById(R.id.deal_image);
        dealDetails = findViewById(R.id.deal_description);

        Button createDeal = findViewById(R.id.create_deal);

        dealImage.setOnClickListener(v -> requestStoragePermission());
        createDeal.setOnClickListener(v -> validateInput());

    }

    private void validateInput() {

        if(dealImage.getDrawable().getConstantState() ==
                Objects.requireNonNull(ContextCompat.getDrawable(CreateDeal.this, R.drawable.deals)).getConstantState())
        {
            Toast.makeText(this, "Deals image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if(dealDetails.getText().toString().isEmpty())
        {
            dealDetails.setError("Please enter deal details");
            dealDetails.requestFocus();
        }
        else
        {
            progressDialog.show();
            saveToDatabase();
        }

    }

    private void saveToDatabase() {

        Bitmap bitmap = ((BitmapDrawable) dealImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(CreateDeal.this.getCacheDir() + File.separator + filename + ".jpg");

        try {
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(byteArrayOutputStream.toByteArray());
            fo.flush();
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part multipartFile = MultipartBody.Part.createFormData("multipartFile", file.getName(), requestBody);

        RetrofitClient.getInstance().getApi()
                .uploadImageToServer("Deals", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {

                            String path = null;

                            try {
                                assert response.body() != null;
                                path = response.body().string();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                            }

                            Deals deals = new Deals();

                            deals.setDealDetails(dealDetails.getText().toString());
                            deals.setDealImage(path);

                            saveGiftsToDatabase(deals);
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(CreateDeal.this, "Error uploading image to server", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e(TAG, t.getMessage());
                        Toast.makeText(CreateDeal.this, "Error uploading image to server", Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void saveGiftsToDatabase(Deals deals) {
        RetrofitClient.getInstance().getApi()
                .createNewDeal(deals)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(CreateDeal.this, "Deals image added successfully...", Toast.LENGTH_SHORT).show();

                            onBackPressed();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(CreateDeal.this, "Can not save deals card, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e(TAG, t.getMessage());
                        Toast.makeText(CreateDeal.this, "Can not save deals card, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        dealImage.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                dealImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

    private void selectImage(Context context)
    {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Take Photo")) {
                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);

            } else if (options[item].equals("Choose from Gallery")) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);
            }
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void requestStoragePermission()
    {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(CreateDeal.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(CreateDeal.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(CreateDeal.this);
        }
    }


}