package com.edu.news.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.edu.news.Activities.HomeActivity;
import com.edu.news.Models.User;
import com.edu.news.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.Executor;

public class profileFragment extends Fragment {
    ImageView image_profile, close;
    TextView save, tv_change;
    MaterialEditText fullname, username, email, password;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;

    public profileFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);


        image_profile = view.findViewById(R.id.profile_image);
        save = view.findViewById(R.id.save);
        tv_change = view.findViewById(R.id.tv_change);
        fullname = view.findViewById(R.id.fullnameProfile);
        username = view.findViewById(R.id.usernameProfile);
        email = view.findViewById(R.id.emailProfile);
        password = view.findViewById(R.id.passwordProfile);
        close = view.findViewById(R.id.close);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        user = FirebaseAuth.getInstance().getCurrentUser();


//        Glide.with(this).load(user.getPhotoUrl()).into(image_profile);
        // Lay thong tin từ fb và hiển thị
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener((Executor) this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                fullname.setText(documentSnapshot.getString("fullname"));
                username.setText(documentSnapshot.getString("username"));
                email.setText(documentSnapshot.getString("email"));
                password.setText(documentSnapshot.getString("password"));
                Glide.with(getActivity()).load(user.getPhotoUrl()).into(image_profile);
            }
        });


//
//
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity().getApplicationContext(), HomeActivity.class));
//                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(fullname.getText().toString(),
                        username.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString());

            }
        });


        return view;

    }

    private void updateProfile(String fullname, String username, String email, String password) {
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
                    Toast.makeText(getActivity().getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                }
            }
        });


        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });


    }


}



