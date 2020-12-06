package com.edu.news.Common;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.edu.news.Activities.HomeActivity;
import com.edu.news.Activities.LoginActivity;
import com.edu.news.Activities.RegisterActivity;
import com.edu.news.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    private static int SLPASH_TIME_OUT = 3000;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();

//        getSupportActionBar().hide();


//        delay splash
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            showMessage("Mời đăng nhập hoặc đăng ký");

        }else {
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            showMessage("Chào mừng quay lại");

        }
                finish();
            }
        }, SLPASH_TIME_OUT);
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null) {
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            showMessage("Mời đăng nhập hoặc đăng ký");
//
//        }else {
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//            showMessage("Chào mừng quay lại");
//
//        }
//    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mHandler != null && mRunnable != null)
//        mHandler.removeCallbacks(mRunnable);
//    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//
//        if (currentUser == null) {
//            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//            showMessage("Mời đăng nhập hoặc đăng ký");
//
//        }else {
//            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//
//        }
//    }
}




