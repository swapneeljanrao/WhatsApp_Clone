package com.mrcoder.view.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mrcoder.R;
import com.mrcoder.adapter.ContactsAdapter;
import com.mrcoder.databinding.ActivityContactsBinding;
import com.mrcoder.model.users.Users;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {

    private final String TAG = "ContactsActivity";
    private ActivityContactsBinding binding;
    private List<Users> list = new ArrayList<>();
    private ContactsAdapter contactsAdapter;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contacts);

        binding.ContactRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        if (firebaseUser != null) {
            getContactList();
        }

    }

    private void getContactList() {
        firestore.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                    String userId = snapshot.getString("userId");
                    String username = snapshot.getString("userName");
                    String imgUrl = snapshot.getString("imageProfile");
                    String description = snapshot.getString("bio");

                    Users users = new Users();
                    users.setUserId(userId);
                    users.setUserName(username);
                    users.setImageProfile(imgUrl);
                    users.setBio(description);


                    if (userId != null && !userId.equals(firebaseUser.getUid())) {

                        list.add(users);
                    }
                }
                contactsAdapter = new ContactsAdapter(list, ContactsActivity.this);
                binding.ContactRecyclerView.setAdapter(contactsAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }
}