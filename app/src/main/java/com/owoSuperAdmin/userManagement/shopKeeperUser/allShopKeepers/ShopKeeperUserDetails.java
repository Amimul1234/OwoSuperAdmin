package com.owoSuperAdmin.userManagement.shopKeeperUser.allShopKeepers;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.configurationsFile.HostAddress;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.userManagement.shopKeeperUser.entity.ShopKeeperUser;
import org.jetbrains.annotations.NotNull;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopKeeperUserDetails extends AppCompatActivity {

    private ShopKeeperUser shopKeeperUser;
    private CollapsingToolbarLayout collapsingToolbar;
    private CircleImageView userProfileImage, userImage;
    private TextView userName, userMobileNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_keeper_user_details);

        shopKeeperUser = (ShopKeeperUser) getIntent().getSerializableExtra("shopKeeperDetails");

        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        userProfileImage = findViewById(R.id.userProfileImage);
        userImage = findViewById(R.id.userImage);
        userName = findViewById(R.id.userName);
        userMobileNumber = findViewById(R.id.userMobileNumber);
        ImageView back_to_home = findViewById(R.id.back_to_home);
        Button disableUser = findViewById(R.id.disableUser);
        Button deleteUser = findViewById(R.id.deleteUser);

        populateData();

        back_to_home.setOnClickListener(v -> onBackPressed());
        disableUser.setOnClickListener(v -> disableUserFromUse());
        deleteUser.setOnClickListener(v -> deleteUserFromDatabase());
    }

    private void deleteUserFromDatabase() {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Delete Shop Keeper");
        progressDialog.setMessage("Please wait while we are deleting shop keeper");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .deleteShopKeeper(shopKeeperUser.getMobileNumber())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ShopKeeperUserDetails.this, "User deleted successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ShopKeeperUserDetails.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ShopKeeperUserDetails.this, "Can not delete user, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ShopKeeperUserDetails.this, "Can not delete user, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("ShopKeeperDetails", "Error occurred, Error is: "+t.getMessage());
                    }
                });

    }

    private void disableUserFromUse() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Disable Shop Keeper");
        progressDialog.setMessage("Please wait while we are disabling shop keeper account");
        progressDialog.show();

        RetrofitClient.getInstance().getApi()
                .disableShopKeeper(shopKeeperUser.getMobileNumber())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(@NotNull Call<ResponseBody> call, @NotNull Response<ResponseBody> response) {
                        if(response.isSuccessful())
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ShopKeeperUserDetails.this, "User disabled successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(ShopKeeperUserDetails.this, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(ShopKeeperUserDetails.this, "Can not disable user, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(ShopKeeperUserDetails.this, "Can not disable user, please try again", Toast.LENGTH_SHORT).show();
                        Log.e("ShopKeeperDetails", "Error occurred, Error is: "+t.getMessage());
                    }
                });
    }

    private void populateData() {

        collapsingToolbar.setTitle(shopKeeperUser.getName());

        if(shopKeeperUser.getImageUri()!=null)
        {
            Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+shopKeeperUser.getImageUri())
                    .into(userProfileImage);

            Glide.with(this).load(HostAddress.HOST_ADDRESS.getAddress()+shopKeeperUser.getImageUri())
                    .into(userImage);
        }

        userName.setText(shopKeeperUser.getName());
        userMobileNumber.setText(shopKeeperUser.getMobileNumber());
    }
}