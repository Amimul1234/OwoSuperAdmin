package com.owoSuperAdmin.offersManagement.addNewOffer;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.categoryManagement.category.entity.CategoryEntity;
import com.owoSuperAdmin.categoryManagement.subCategory.addSubCategory.CategoryCustomSpinnerAdapter;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.offersManagement.entity.OffersEntity;
import com.owoSuperAdmin.owoshop.R;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOffersActivity extends AppCompatActivity {

    private final String TAG = "Create Offer Activity";
    private final int STORAGE_PERMISSION_CODE = 1;
    private final Calendar myCalendar = Calendar.getInstance();

    private ImageView createOfferImage;
    private EditText offerStartDate, offerEndDate;
    private Date StartDate, EndDate;
    private Spinner offer_is_for_spinner;
    private ProgressDialog loading;
    private SwitchMaterial enable_offer_switch;

    private final List<CategoryEntity> categoryEntityList = new ArrayList<>();

    private Spinner categorySelectorSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offers);

        createOfferImage = findViewById(R.id.create_offers_image);
        offerStartDate = findViewById(R.id.offer_start_date);
        offerEndDate = findViewById(R.id.offer_end_date);
        offer_is_for_spinner = findViewById(R.id.offer_is_for);
        enable_offer_switch = findViewById(R.id.enable_offer_switch);

        categorySelectorSpinner = findViewById(R.id.categorySelectorSpinner);

        loading = new ProgressDialog(this);

        Button createOfferBtn = findViewById(R.id.create_offer_btn);
        ImageView offer_start_date_picker = findViewById(R.id.start_date_picker);
        ImageView offer_end_date_picker = findViewById(R.id.end_date_picker);
        ImageView back_to_home = findViewById(R.id.back_to_home);

        fetchCategories();

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(1);
        };

        final DatePickerDialog.OnDateSetListener date2 = (view, year, monthOfYear, dayOfMonth) -> {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(2);
        };

        offer_start_date_picker.setOnClickListener(v -> new DatePickerDialog(CreateOffersActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());


        offer_end_date_picker.setOnClickListener(v -> new DatePickerDialog(CreateOffersActivity.this, date2, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        back_to_home.setOnClickListener(v -> {
            Intent intent = new Intent(CreateOffersActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        createOfferImage.setOnClickListener(v -> requestStoragePermission());

        createOfferBtn.setOnClickListener(v -> ValidateOfferData());
    }

    private void updateLabel(int state) {

        String myFormat = "dd/MM/yyyy";

        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        SimpleDateFormat format2 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        if(state == 1)
        {
            offerStartDate.setText(sdf.format(myCalendar.getTime()));
            try {
                StartDate = format2.parse(myCalendar.getTime().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        else if(state == 2)
        {
            offerEndDate.setText(sdf.format(myCalendar.getTime()));
            try {
                EndDate = format2.parse(myCalendar.getTime().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void ValidateOfferData() {

        if(createOfferImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(CreateOffersActivity.this, R.drawable.offers)).getConstantState())
        {
            Toast.makeText(this, "Offer image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (StartDate == null)
        {
            Toast.makeText(this, "Please enter offer start date...", Toast.LENGTH_SHORT).show();
        }
        else if (EndDate == null)
        {
            Toast.makeText(this, "Please enter offer end date...", Toast.LENGTH_SHORT).show();
        }

        else {

            if(StartDate.after(EndDate))
            {
                Toast.makeText(this, "Start date can not be less than the end date", Toast.LENGTH_SHORT).show();
                return;
            }
            StoreOfferInformation();
        }
    }

    private void StoreOfferInformation()
    {

        loading.setTitle("Add A New Offer");
        loading.setMessage("Please wait, we are adding the new offer...");
        loading.setCanceledOnTouchOutside(false);
        loading.show();

        Bitmap bitmap = ((BitmapDrawable) createOfferImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(CreateOffersActivity.this.getCacheDir() + File.separator + filename + ".jpg");

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
                .uploadImageToServer("Offers", multipartFile)
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

                            OffersEntity offersEntity = new OffersEntity();

                            offersEntity.setOffer_start_date(StartDate);
                            offersEntity.setOffer_end_date(EndDate);
                            offersEntity.setOffer_is_for(offer_is_for_spinner.getSelectedItem().toString());
                            offersEntity.setOffer_image(path);
                            offersEntity.setCategoryEntity((CategoryEntity) categorySelectorSpinner.getSelectedItem());//Should obviously change letter
                            offersEntity.setEnabled(enable_offer_switch.isChecked());

                            SaveOfferInfoToDatabase(offersEntity);
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(CreateOffersActivity.this, "Error uploading image to server", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        loading.dismiss();
                        Log.e(TAG, t.getMessage());
                        Toast.makeText(CreateOffersActivity.this, "Error uploading image to server", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void SaveOfferInfoToDatabase(OffersEntity offersEntity)
    {
        RetrofitClient.getInstance().getApi()
                .addAnOffer(offersEntity)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            loading.dismiss();
                            Toast.makeText(CreateOffersActivity.this, "Offer is added successfully...", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(CreateOffersActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(CreateOffersActivity.this, "Error creating offer..., try again", Toast.LENGTH_SHORT).show();
                            try {
                                assert response.body() != null;
                                Log.e(TAG, response.body().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        loading.dismiss();
                        Log.e(TAG, t.getMessage());
                        Toast.makeText(CreateOffersActivity.this, "Failed to create offer", Toast.LENGTH_SHORT).show();
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
                        createOfferImage.setImageBitmap(selectedImage);
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
                                createOfferImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
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

                        ActivityCompat.requestPermissions(CreateOffersActivity.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(CreateOffersActivity.this);
                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(CreateOffersActivity.this);
        }
    }

    private void fetchCategories()
    {

        loading.setTitle("All Categories");
        loading.setMessage("Please wait while we are fetching all the categories");
        loading.show();

        RetrofitClient.getInstance().getApi()
                .getAllCategories()
                .enqueue(new Callback<List<CategoryEntity>>() {
                    @Override
                    public void onResponse(@NotNull Call<List<CategoryEntity>> call, @NotNull Response<List<CategoryEntity>> response) {
                        if(response.isSuccessful())
                        {
                            loading.dismiss();
                            categoryEntityList.clear();
                            assert response.body() != null;
                            categoryEntityList.addAll(response.body());
                            CategoryCustomSpinnerAdapter categoryCustomSpinnerAdapter = new CategoryCustomSpinnerAdapter(CreateOffersActivity.this,
                                    categoryEntityList);
                            categorySelectorSpinner.setAdapter(categoryCustomSpinnerAdapter);
                        }
                        else
                        {
                            loading.dismiss();
                            Toast.makeText(CreateOffersActivity.this, "Can not get categories, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<List<CategoryEntity>> call, @NotNull Throwable t) {
                        loading.dismiss();
                        Log.e("Add Offer", "Error is: "+t.getMessage());
                        Toast.makeText(CreateOffersActivity.this, "Can not get categories, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
