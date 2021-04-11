package com.owoSuperAdmin.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.owoSuperAdmin.adminHomePanel.HomeActivity;
import com.owoSuperAdmin.adminManagement.entity.AdminLoginWrapper;
import com.owoSuperAdmin.adminManagement.entity.AdminPermissions;
import com.owoSuperAdmin.network.RetrofitClient;
import com.owoSuperAdmin.owoshop.R;
import com.owoSuperAdmin.utilities.PasswordHashing;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText email_address, password;
    private ImageView visibility;
    private Boolean isShowPassword = false;
    public String login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Making activity full screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                insetsController.hide(WindowInsets.Type.statusBars());
            }
        } else {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }

        Button loginButton = findViewById(R.id.login_btn);
        email_address = findViewById(R.id.admin_email_address);
        password = findViewById(R.id.admin_password);
        visibility = findViewById(R.id.show_password);

        loginButton.setOnClickListener(v -> verify());

        visibility.setOnClickListener(v -> {
            if (isShowPassword) {
                password.setTransformationMethod(new PasswordTransformationMethod());
                visibility.setImageResource(R.drawable.ic_visibility_off);
                isShowPassword = false;
            }
            else{
                password.setTransformationMethod(null);
                visibility.setImageResource(R.drawable.ic_visibility);
                isShowPassword = true;
            }
        });

    }

    private void verify() {

        String email = email_address.getText().toString();
        login_password = password.getText().toString();

        if(email.isEmpty())
        {
            email_address.setError("Please enter an email address");
            email_address.requestFocus();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_address.setError("Please enter a valid email address");
            email_address.requestFocus();
        }
        else if(login_password.isEmpty())
        {
            password.setError("Please enter a password");
            password.requestFocus();
        }
        else
        {
            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Sing In");
            progressDialog.setMessage("Signing in...please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            loginCheck(email, PasswordHashing.sha256(login_password), progressDialog);

        }
    }

    private void loginCheck(String email, String login_password, ProgressDialog progressDialog)
    {
        RetrofitClient.getInstance().getApi()
                .getAdminCredential(email)
                .enqueue(new Callback<AdminLoginWrapper>() {
                    @Override
                    public void onResponse(@NotNull Call<AdminLoginWrapper> call, @NotNull Response<AdminLoginWrapper> response) {
                        if(response.isSuccessful())
                        {
                            AdminLoginWrapper adminLoginWrapper = response.body();

                            assert adminLoginWrapper != null;
                            if(login_password.equals(adminLoginWrapper.getAdminLogin().getAdminPassword()))
                            {
                                progressDialog.dismiss();

                                Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                                AdminCredentials.adminName = adminLoginWrapper.getAdminLogin().getAdminName();
                                AdminCredentials.adminPassword = adminLoginWrapper.getAdminLogin().getAdminPassword();

                                for(AdminPermissions adminPermissions : adminLoginWrapper.getAdminPermissionsList())
                                {
                                    AdminCredentials.adminPermissionList.add(adminPermissions.getPermission());
                                }

                                Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);
                            }

                            else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NotNull Call<AdminLoginWrapper> call, @NotNull Throwable t) {
                        progressDialog.dismiss();
                        Log.e("Login", "Error while logging in, Error is: "+t.getMessage());
                        Toast.makeText(LoginActivity.this, "Error occurred while logging in", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
