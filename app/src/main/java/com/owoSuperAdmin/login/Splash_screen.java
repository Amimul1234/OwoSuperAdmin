package com.owoSuperAdmin.login;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;
import com.owoSuperAdmin.owoshop.R;

public class Splash_screen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseMessaging.getInstance().subscribeToTopic("e-commerce")
                .addOnCompleteListener(task -> {

                    String msg = "Subscribed";

                    if (!task.isSuccessful()) {
                        msg = "Subscription failed";
                    }

                    Log.d("FCM", msg);
                    Toast.makeText(Splash_screen.this, msg, Toast.LENGTH_SHORT).show();
                });

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

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent mainIntent = new Intent(Splash_screen.this, LoginActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(mainIntent);
            finish();

        }, 3000);
    }

}