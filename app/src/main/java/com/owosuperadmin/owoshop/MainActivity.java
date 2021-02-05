package com.owosuperadmin.owoshop;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText email_address, password;
    private FirebaseAuth mAuth;
    private ImageView visibility;
    private Boolean isShowPassword = false;
    private Boolean loginButtonHandler = true;
    private ProgressBar progressBar;
    static String login_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();

        loginButton=(Button)findViewById(R.id.login_btn);
        email_address = (EditText)findViewById(R.id.admin_email_address);
        password = (EditText)findViewById(R.id.admin_password);
        visibility = findViewById(R.id.show_password);
        progressBar = findViewById(R.id.log_in_progress);
        loginButton.setEnabled(loginButtonHandler);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonHandler = false;
                varify();
            }
        });

        visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isShowPassword) {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                    visibility.setImageResource(R.drawable.ic_visibility_off);
                    isShowPassword = false;

                }else{
                    password.setTransformationMethod(null);
                    visibility.setImageResource(R.drawable.ic_visibility);
                    isShowPassword = true;
                }
            }
        });

    }

    private void varify() {

        String email = email_address.getText().toString();
        login_password = password.getText().toString();

        if(email.isEmpty())
        {
            email_address.setError("Please enter an email address");
            email_address.requestFocus();
            loginButtonHandler = true;
        }

        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            email_address.setError("Please enter a valid email address");
            email_address.requestFocus();
            loginButtonHandler = true;
        }

        else if(login_password.isEmpty())
        {
            password.setError("Please enter a password");
            password.requestFocus();
            loginButtonHandler = true;
        }

        else
        {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(email, login_password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();

                                Toast.makeText(MainActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(MainActivity.this,HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);

                            } else {
                                Toast.makeText(MainActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                loginButtonHandler = true;
                                progressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
        }
    }


}
