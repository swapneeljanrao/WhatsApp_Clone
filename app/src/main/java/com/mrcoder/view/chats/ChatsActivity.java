package com.mrcoder.view.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrcoder.R;
import com.mrcoder.adapter.ChatsAdapter;
import com.mrcoder.databinding.ActivityChatsBinding;
import com.mrcoder.model.chat.Chats;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChatsActivity extends AppCompatActivity {

    private ActivityChatsBinding binding;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private String receiverId;
    private ChatsAdapter adapter;
    private List<Chats> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chats);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        Intent intent = getIntent();
        receiverId = intent.getStringExtra("userId");
        String username = intent.getStringExtra("userName");
        String userProfile = intent.getStringExtra("userProfile");

        if (receiverId != null) {
            binding.ChatTvContactName.setText(username);
            Glide.with(this).load(userProfile).into(binding.chatsUserImage);
        }
        binding.btnkBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        binding.etTypeMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(binding.etTypeMessage.getText().toString())) {
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_mic));
                } else {
                    binding.btnSend.setImageDrawable(getDrawable(R.drawable.ic_send));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        intiBtnClick();

        list = new ArrayList<>();
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, true);
        linearLayout.setStackFromEnd(true);
        binding.ChatsScreenRecycler.setLayoutManager(linearLayout);

        readChats();
    }

    private void readChats() {
        try {

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            reference.child("Chats").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    list.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Chats chats = snapshot.getValue(Chats.class);
                        if (chats.getSender().equals(firebaseUser.getUid()) && chats.getReceiver().equals(receiverId)) {

                            list.add(chats);
                        }
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter = new ChatsAdapter(list, ChatsActivity.this);
                        binding.ChatsScreenRecycler.setAdapter(adapter);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void intiBtnClick() {
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!TextUtils.isEmpty(binding.etTypeMessage.getText().toString())) {
                    sendTextMessage(binding.etTypeMessage.getText().toString());

                    binding.etTypeMessage.setText("");
                }
            }
        });

        binding.btnkBackChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendTextMessage(String textMessage) {

        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(date);

        Calendar currentDateTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("hh:mm a");
        String currentTime = df.format(currentDateTime);
        Chats chats = new Chats(
                today + "," + currentDateTime,
                textMessage, "TEXT",
                firebaseUser.getUid(), receiverId);

        reference.child("Chats").push().setValue(chats).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Log.d("Send", "onSuccess");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("Send", "onFailure" + e.getMessage());
            }
        });

        DatabaseReference chatRef1 = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid()).child(receiverId);
        chatRef1.child("chatid").setValue(receiverId);

        DatabaseReference chatRef2 = FirebaseDatabase.getInstance().getReference("ChatList").child(receiverId).child(firebaseUser.getUid());
        chatRef2.child("chatid").setValue(firebaseUser.getUid());

    }
}