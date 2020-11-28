package com.owosuperadmin.owoshop;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class CreateNewAdminActivity extends AppCompatActivity {

    private Switch approveShop,maintainShop,addProducts,
            updateProducts,createOffers,maintainUsers,messaging;
    private EditText newAdminMobileNumber, newAdminPassword, newAdminConfirmPassword, newAdminName;
    private Button createNewAdminBtn;
    private ProgressBar progressBar;
    private ImageView s_password, s_c_password, profileImage, back_to_home;
    private Uri imageuri;
    private String myUrl = "", admin_email;
    private Boolean isShowPassword = false, isShowConfirmPassword = false;
    private StorageTask uploadTask;
    private StorageReference storageProfilePictureRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private int STORAGE_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_admin);

        approveShop = (Switch)findViewById(R.id.approve_shop);
        maintainShop = (Switch)findViewById(R.id.maintain_shop);
        addProducts = (Switch)findViewById(R.id.add_products);
        updateProducts = (Switch)findViewById(R.id.update_products);
        createOffers = (Switch)findViewById(R.id.create_offers);
        maintainUsers = (Switch)findViewById(R.id.maintain_users);
        messaging = (Switch)findViewById(R.id.messaging);
        newAdminMobileNumber = (EditText)findViewById(R.id.new_admin_mobile_number);
        newAdminPassword = (EditText)findViewById(R.id.new_admin_password);
        newAdminConfirmPassword = (EditText)findViewById(R.id.new_admin_confirm_password);
        createNewAdminBtn=(Button)findViewById(R.id.create_new_admin_btn);
        s_password = findViewById(R.id.show_password);
        s_c_password = findViewById(R.id.show_confirmed_password);
        progressBar = findViewById(R.id.complete_progress);
        profileImage = findViewById(R.id.profileImage);
        newAdminName = findViewById(R.id.new_admin_name);
        back_to_home = findViewById(R.id.back_to_home);


        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("SemiAdmins");

        back_to_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateNewAdminActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {//For selecting the profile image

            @Override
            public void onClick(View v) {
                requestStoragePermission();
            }

        });



        s_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowPassword) {
                    newAdminPassword.setTransformationMethod(new PasswordTransformationMethod());
                    s_password.setImageResource(R.drawable.ic_visibility_off);
                    isShowPassword = false;

                }else{
                    newAdminPassword.setTransformationMethod(null);
                    s_password.setImageResource(R.drawable.ic_visibility);
                    isShowPassword = true;
                }
            }
        });

        s_c_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowConfirmPassword) {
                    newAdminConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    s_c_password.setImageResource(R.drawable.ic_visibility_off);
                    isShowConfirmPassword = false;

                }else{
                    newAdminConfirmPassword.setTransformationMethod(null);
                    s_c_password.setImageResource(R.drawable.ic_visibility);
                    isShowConfirmPassword = true;
                }
            }
        });

        createNewAdminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification();
            }
        });

    }

    private void uploadImageofAdmin(final String Admin_name, final String new_phone, final Boolean approve_shop, final Boolean maintain_shop, final Boolean add_products, final Boolean update_products,
                                    final Boolean create_offers, final Boolean maintain_users, final Boolean messaging_) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Upload Profile Image");
        progressDialog.setMessage("Please wait while we are uploading profile image...");
        progressDialog.setCanceledOnTouchOutside(false);

        if(imageuri!=null)
        {
            final StorageReference fileRef = storageProfilePictureRef.child(new_phone+".jpg");

            uploadTask = fileRef.putFile(imageuri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful())
                    {
                        throw task.getException();
                    }

                    return fileRef.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful())
                    {
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        uploadProfileInfo(Admin_name, new_phone, approve_shop, maintain_shop, add_products, update_products,
                                create_offers, maintain_users, messaging_);
                    }

                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(CreateNewAdminActivity.this, "Error...Can not upload image", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else
        {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadProfileInfo(String Admin_name, String phone, Boolean approve_shop, Boolean maintain_shop, Boolean add_products, Boolean update_products,
                                   Boolean create_offers, Boolean maintain_users, Boolean messaging_) {


        final com.owosuperadmin.model.semi_admins new_Semi_Admin = new com.owosuperadmin.model.semi_admins(myUrl, Admin_name, phone,approve_shop, maintain_shop,
                add_products, update_products, create_offers, maintain_users, messaging_);

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user1 = mAuth.getCurrentUser();
        admin_email = user1.getEmail();

        progressBar.setVisibility(View.VISIBLE);

        if(user1 != null)
        {

            final DatabaseReference myRef = database.getReference().child("Semi Admins").child(phone);

            String email = newAdminMobileNumber.getText().toString()+"@gmail.com";

            mAuth.createUserWithEmailAndPassword(email, phone)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                Toast.makeText(CreateNewAdminActivity.this, "New admin authentication successful", Toast.LENGTH_SHORT).show();

                                FirebaseAuth.getInstance().signOut();

                                FirebaseAuth.getInstance().signInWithEmailAndPassword(admin_email, MainActivity.login_password)
                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {

                                                if(task.isSuccessful())
                                                {
                                                    myRef.setValue(new_Semi_Admin).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful())
                                                            {
                                                                Toast.makeText(CreateNewAdminActivity.this, "New Admin added successfully", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(CreateNewAdminActivity.this, HomeActivity.class);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                startActivity(intent);
                                                                finish();
                                                            }

                                                            else
                                                            {
                                                                Toast.makeText(CreateNewAdminActivity.this, "Can not write semi admin access to database", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                            }
                            else {
                                Toast.makeText(CreateNewAdminActivity.this, "Can not authenticate new admin", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });

        }




    }

    private void verification() {

        String Admin_name = newAdminName.getText().toString();
        String mobile = newAdminMobileNumber.getText().toString();
        String password = newAdminPassword.getText().toString();
        String confirmpassword = newAdminConfirmPassword.getText().toString();

        if(Admin_name.isEmpty())
        {
            newAdminName.setError("Please enter a valid name");
            newAdminName.requestFocus();
        }

        else if(mobile.isEmpty())
        {
            newAdminMobileNumber.setError("Please enter your mobile number");
            newAdminMobileNumber.requestFocus();
        }
        else if(mobile.length() < 11)
        {
            newAdminMobileNumber.setError("Please enter a valid mobile number");
            newAdminMobileNumber.requestFocus();
        }
        else if(password.isEmpty())
        {
            newAdminPassword.setError("Please enter a password");
            newAdminPassword.requestFocus();
        }
        else if(confirmpassword.isEmpty())
        {
            newAdminConfirmPassword.setError("Please enter password to confirm");
            newAdminConfirmPassword.requestFocus();
        }

        else if(!confirmpassword.equals(password))
        {
            newAdminConfirmPassword.setError("Password did not match");
            newAdminConfirmPassword.requestFocus();
        }
        else
        {

            String phone = newAdminMobileNumber.getText().toString();

            Boolean approve_shop, maintain_shop, add_products, update_products,
                    create_offers, maintain_users, messaging_;

            approve_shop = approveShop.isChecked();
            maintain_shop = maintainShop.isChecked();
            add_products = addProducts.isChecked();
            update_products = updateProducts.isChecked();
            create_offers = createOffers.isChecked();
            maintain_users = maintainUsers.isChecked();
            messaging_ = messaging.isChecked();

            if(!approve_shop && !maintain_shop && !add_products &&
                    !update_products && !create_offers && !maintain_users && !messaging_)
            {
                Toast.makeText(CreateNewAdminActivity.this, "Please enter a permission", Toast.LENGTH_SHORT).show();
            }

            else
            {
                uploadImageofAdmin(Admin_name, phone, approve_shop, maintain_shop, add_products, update_products, create_offers
                    ,maintain_users, messaging_);
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null)
        {
            imageuri = data.getData();
            profileImage.setImageURI(imageuri);
        }
        else
        {
            Toast.makeText(this, "Try again", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of taking image from gallery")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(CreateNewAdminActivity.this,
                                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                ImagePicker.Companion.with(CreateNewAdminActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(512)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(540, 540)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
