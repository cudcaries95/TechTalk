package com.edu.news.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.edu.news.Adapter.PostAdapter;
import com.edu.news.Models.Post;
import com.edu.news.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class saveFragment extends Fragment {

   List<String> mySaves;
    RecyclerView postRecyclerView_save ;
    PostAdapter postAdapter_save ;
    List<Post> postList_save;

    FirebaseUser firebaseUser;


    public saveFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_save, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        postRecyclerView_save  = fragmentView.findViewById(R.id.saveRV);
        postRecyclerView_save.setHasFixedSize(true);
        postRecyclerView_save.setLayoutManager(new LinearLayoutManager(getActivity()));

        postList_save = new ArrayList<>();
        postAdapter_save = new PostAdapter(getActivity(),postList_save);
        postRecyclerView_save.setAdapter(postAdapter_save);

        mySaves();

        return fragmentView;
    }

    public void mySaves(){
        mySaves = new ArrayList<>();
         DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Saves")
                .child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    mySaves.add(snapshot.getKey());

                }
                readSaves();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void readSaves(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList_save.clear();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Post post = snapshot.getValue(Post.class);

                    for (String id: mySaves){
                        if (post.getPostKey().equals(id)){
                            postList_save.add(post) ;
                        }
                    }
                }

                postAdapter_save = new PostAdapter(getActivity(),postList_save);
                postRecyclerView_save.setAdapter(postAdapter_save);
                postAdapter_save.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
