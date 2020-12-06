package com.edu.news.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.edu.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText regName, regUsername, regEmail, regPassword;
    private Button btnDangKy;
    private ProgressBar loadingProgressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mStore;
    TextView tvDangNhap;
    ImageView ImgUserPhoto;
    String userID;

    static int PReqCode = 1;
    static int REQUESCODE = 1;
    Uri pickedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Views
        ImgUserPhoto = findViewById(R.id.regUserPhoto);
        regUsername = findViewById(R.id.regUsername);
        regName = findViewById(R.id.regName);
        regPassword = findViewById(R.id.regPassword);
        regEmail = findViewById(R.id.regEmail);
        btnDangKy = findViewById(R.id.regBtn);
        tvDangNhap = findViewById(R.id.tvDangnhap);
        loadingProgressBar = findViewById(R.id.regProgressBar);
        loadingProgressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        btnDangKy.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             btnDangKy.setVisibility(View.INVISIBLE);
                                             loadingProgressBar.setVisibility(View.VISIBLE);
                                             final String name = regName.getText().toString();
                                             final String username = regUsername.getText().toString();
                                             final String email = regEmail.getText().toString();
                                             final String password = regPassword.getText().toString();


                                             if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() ) {

                                                 showMessage("Nhập đủ thông tin và avatar");
                                                 btnDangKy.setVisibility(View.VISIBLE);
                                                 loadingProgressBar.setVisibility(View.INVISIBLE);
                                             } else {
                                                 CreateUserAccount(username, name, email, password);
                                             }
                                         }
                                     }
        );


        tvDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        ImgUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();

                } else {
                    openGallery();
                }
            }
        });

    }

    private void CreateUserAccount(final String username, final String name, final String email, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    showMessage("Tạo tài khoản thành công");

                    userID = mAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = mStore.collection("users").document(userID);
                    Map<String,Object> user = new HashMap<>();    
                    user.put("fullname",name);
                    user.put("email",email);
                    user.put("username",username);
                    user.put("password",password);


                    documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
//                         showMessage("Tạo profile thành công user:" + userID);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showMessage("Lỗi:" + e.toString());
                        }
                    });

                    updateUserInfo(name, pickedImgUri, mAuth.getCurrentUser());


                } else {
                    // tao tai khoan that bai
                    showMessage("Tạo thất bại" + task.getException().getMessage().toString());
                    btnDangKy.setVisibility(View.VISIBLE);
                    loadingProgressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUserInfo(final String name, Uri pickedImgUri ,final FirebaseUser currentUser) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("users_photos");
        final StorageReference imageFilePath = mStorage.child(pickedImgUri.getLastPathSegment());
        imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //up anh thanh cong
                //co the get our image url

                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        // contain user image url

                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri)
                                .build();


                        currentUser.updateProfile(profileUpdate)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            // info user update thanh cong
                                            showMessage("Đăng ký thành công");
                                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

                                        }

                                    }
                                });
                    }
                });
            }
        });
    }



    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    //phuong thuc lay anh tu thu vien va set avatar
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null) {
            pickedImgUri = data.getData();
            ImgUserPhoto.setImageURI(pickedImgUri);
        }
    }

    private void openGallery() {
        //mo Gallery
        Intent GalleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent, REQUESCODE);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(RegisterActivity.this, "Dề nghị cho phép quyền truy cập ảnh", Toast.LENGTH_SHORT).show();


            } else {
                ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PReqCode);
                Toast.makeText(RegisterActivity.this, "Chọn ảnh", Toast.LENGTH_SHORT).show();

            }
        } else
            openGallery();
    }


}













