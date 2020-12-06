package com.edu.news.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.edu.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class UserProfile extends AppCompatActivity {
    // Khai báo
    ImageView image_profile, close;
    TextView save;
    Button delete;
    MaterialEditText fullname, username, email, password;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Anh xa
        image_profile = findViewById(R.id.profile_image);
        save = findViewById(R.id.update_btn);
//        delete = findViewById(R.id.delete_btn);
//        tv_change = findViewById(R.id.tv_change);
        fullname = findViewById(R.id.fullnameProfile);
        username = findViewById(R.id.usernameProfile);
        email = findViewById(R.id.emailProfile);
        password = findViewById(R.id.passwordProfile);
        close = findViewById(R.id.close);

        // firebase
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();

//        Glide.with(this).load(user.getPhotoUrl()).into(image_profile);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullname.setText(documentSnapshot.getString("fullname"));
                username.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
                password.setText(documentSnapshot.getString("password"));
                Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(image_profile);
            }
        });

        // Click nut close
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
//                finish();
            }
        });

        // Click nut save
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(fullname.getText().toString(),
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString());




            }
        });

//        delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
//                            Toast.makeText(getApplicationContext(), "Xóa tài khoản thành công", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
//
//            }
//        });


    }

    private void updateProfile(String fullname, String username, String email, final String password) {
        DocumentReference documentReference = fStore.collection("users").document(userId);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fullname", fullname);
        hashMap.put("username", username);
        hashMap.put("email", email);
        hashMap.put("password", password);
        documentReference.update(hashMap);

        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> tasks) {
                            if (tasks.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"Lỗi",Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                }
            }
        });
//






    }


}
