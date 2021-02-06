package com.owoSuperAdmin.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.owoSuperAdmin.homePanel.HomeActivity;
import com.owoSuperAdmin.owoshop.R;
import java.util.Objects;


@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {

    private EditText email_address, password;
    private FirebaseAuth mAuth;
    private ImageView visibility;
    private Boolean isShowPassword = false;
    public static String login_password;

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
        mAuth = FirebaseAuth.getInstance();

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
            progressDialog.setMessage("Signing In...please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email, login_password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                            Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            finish();
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    });
        }
    }
}
