package com.owoSuperAdmin.pushNotification;

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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.owoSuperAdmin.configurationsFile.HostAddress;
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

public class CloudMessagingActivity extends AppCompatActivity {

    private final String TAG = "Push Notification";
    private final int STORAGE_PERMISSION_CODE = 1;

    private ImageView notificationImage;
    private EditText notificationTitle, notificationBody;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_messaging);

        progressDialog = new ProgressDialog(this);

        ImageView backButton = findViewById(R.id.backButton);
        Button sendPushNotification = findViewById(R.id.send_push_notification);
        notificationTitle = findViewById(R.id.push_notification_title);
        notificationBody = findViewById(R.id.push_notification_body);
        notificationImage = findViewById(R.id.push_notification_image);

        backButton.setOnClickListener(v -> onBackPressed());
        notificationImage.setOnClickListener(v -> requestStoragePermission());
        sendPushNotification.setOnClickListener(v -> validateNotificationData());
    }

    private void validateNotificationData()
    {
        if(notificationTitle.getText().toString().isEmpty())
        {
            notificationTitle.setError("Notification title can not be empty");
            notificationTitle.requestFocus();
        }
        else if(notificationBody.getText().toString().isEmpty())
        {
            notificationBody.setError("Notification Body can not be empty");
            notificationBody.requestFocus();
        }
        else if(notificationImage.getDrawable().getConstantState() == Objects.requireNonNull(ContextCompat.getDrawable(
                CloudMessagingActivity.this, R.drawable.send_push_notification)).getConstantState())
        {
            Toast.makeText(this, "Please select notification image", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.setTitle("Send push notification to clients");
            progressDialog.setMessage("Please wait while we are sending notification to client");
            progressDialog.setCancelable(false);
            progressDialog.show();

            sendToDatabase();
        }
    }

    private void sendToDatabase() {
        Bitmap bitmap = ((BitmapDrawable) notificationImage.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        String filename = UUID.randomUUID().toString();

        File file = new File(CloudMessagingActivity.this.getCacheDir() + File.separator + filename + ".jpg");

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
                .uploadImageToServer("Notifications", multipartFile)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            try {
                                assert response.body() != null;
                                String path = HostAddress.HOST_ADDRESS.getAddress() + response.body().string();


                                NotificationData notificationData = new NotificationData();

                                notificationData.setContent(notificationBody.getText().toString());
                                notificationData.setSubject(notificationTitle.getText().toString());
                                notificationData.setImageUrl(path);


                                RetrofitClient.getInstance().getApi()
                                        .sendNotification(notificationData, "e-commerce")
                                        .enqueue(new Callback<ResponseBody>() {
                                            @Override
                                            public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                                                if(response.isSuccessful())
                                                {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(CloudMessagingActivity.this, "Notification sent successfully", Toast.LENGTH_SHORT).show();
                                                    onBackPressed();
                                                }
                                                else
                                                {
                                                    progressDialog.dismiss();
                                                    Log.e(TAG, "Error occurred, Error code: "+response.code());
                                                    Toast.makeText(CloudMessagingActivity.this, "Notification was not sent", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                                                progressDialog.dismiss();
                                                Log.e(TAG, "Error occurred, Error is: "+t.getMessage());
                                                Toast.makeText(CloudMessagingActivity.this, "Notification was not sent", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e(TAG, "Error occurrend, Error is: "+t.getMessage());
                        Toast.makeText(CloudMessagingActivity.this, "Could not sent notification", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void requestStoragePermission()
    {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", (dialog, which) -> {

                        ActivityCompat.requestPermissions(CloudMessagingActivity.this,
                                new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                        selectImage(CloudMessagingActivity.this);

                    })
                    .setNegativeButton("cancel", (dialog, which) -> dialog.dismiss())
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

            selectImage(CloudMessagingActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        notificationImage.setImageBitmap(selectedImage);
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
                                notificationImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }

}
