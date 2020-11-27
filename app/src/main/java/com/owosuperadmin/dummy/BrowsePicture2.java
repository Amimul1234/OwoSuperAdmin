package com.owosuperadmin.dummy;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.owosuperadmin.Network.RetrofitClient;
import com.owosuperadmin.model.Owo_product;
import com.owosuperadmin.owoshop.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BrowsePicture2 extends AppCompatActivity {

    final int REQUEST_EXTERNAL_STORAGE = 100;
    String downloadImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_picture);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(BrowsePicture2.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(BrowsePicture2.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
//                    return;
                } else {
                    launchGalleryIntent();
                }
            }
        });
    }

    public void launchGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    launchGalleryIntent();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_EXTERNAL_STORAGE && resultCode == RESULT_OK) {

            final ImageView imageView = findViewById(R.id.image_view);
            final List<Bitmap> bitmaps = new ArrayList<>();
            ClipData clipData = data.getClipData();

            if (clipData != null) {
                //multiple images selecetd
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();


                    Calendar calendar= Calendar.getInstance();

                    SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
                    String saveCurrentDate = currentDate.format(calendar.getTime());

                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
                    String saveCurrentTime = currentTime.format(calendar.getTime());

                    String productRandomKey = saveCurrentDate+saveCurrentTime;



                    StorageReference ProductImagesRef = FirebaseStorage.getInstance().getReference().child("ProductImage");

                    final StorageReference filePath = ProductImagesRef.child(imageUri.getLastPathSegment() + productRandomKey +".jpg");

                    final UploadTask uploadTask = filePath.putFile(imageUri);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            String message=e.toString();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if (!task.isSuccessful())
                                    {
                                        throw  task.getException();
                                    }
                                    downloadImageUrl=filePath.getDownloadUrl().toString();
                                    return filePath.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if(task.isSuccessful())
                                    {
                                        downloadImageUrl=task.getResult().toString();
                                        SaveProductInfoToDatabase(saveCurrentDate, saveCurrentTime);
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }
    }


    private void SaveProductInfoToDatabase(String saveCurrentDate, String saveCurrentTime) {

        Owo_product owo_product = new Owo_product("Beauty and Bodycare", "Bags and Luggage", 500.12, 30.00, 600, "The Science Book: Everything You Need to Know About the World and How It Works encapsulates centuries of scientific thought in one volume. Natural phenomena, revolutionary inventions, scientific facts, and the most up-to-date questions are all explained in detailed text that is complemented by visually arresting graphics.   Six major sections ranging from the universe and the planet Earth to biology, chemistry, physics, and mathematics are further broken down into subsections that encompass everything from microscopic life to nuclear power.   The Science Book covers a wide range of scientific areas, providing both a general overview of topics for the browsing reader, and more specific information for those who wish to obtain in-depth insight into a particular subject area. Natural phenomena, revolutionary inventions, scientific facts, and up-to-date questions are explained in detailed texts. The vivid illustrations, pictures, and graphics throughout the book make the information even more accessible and comprehensible.   Within the book, the theory of the universe and the character of the earth are detailed, along with an overview of the diverse living organisms that can be found on Earth. The technical developments and achievements of humankind are discussed and we pay particular attention to subjects of current interest, like climate change and genetic engineering.   The well-structured organization of this book with its numerous sections and chapters offers the reader an entertaining introduction into the large field of natural sciences and allows just as well for quick reference. Events and issues of special significance are discussed in greater detail in side bars of 3 different kinds. Numerous cross-references within the chapters and to other sections of the book emphasize the many links between the individual scientific fields. Illustrative elements, such as 3-D-graphics and pictograms and the great variety of photographic material make even the most complex information easy",
                saveCurrentDate, saveCurrentTime, "Bags", "Calvin Klein", downloadImageUrl);

        Call<Owo_product> call = RetrofitClient
                .getInstance()
                .getApi()
                .createProduct(owo_product);

        call.enqueue(new Callback<Owo_product>() {
            @Override
            public void onResponse(Call<Owo_product> call, Response<Owo_product> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(BrowsePicture2.this, "Product Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<Owo_product> call, Throwable t) {
                Toast.makeText(BrowsePicture2.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}

