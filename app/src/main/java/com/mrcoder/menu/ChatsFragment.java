package com.mrcoder.menu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mrcoder.R;
import com.mrcoder.adapter.ChatListAdapter;
import com.mrcoder.databinding.FragmentChatsBinding;
import com.mrcoder.model.ChatList;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatsFragment extends Fragment {

    private static final String TAG = "ChatsFragment";

    public ChatsFragment() {
        // Required empty public constructor
    }


    FirebaseUser firebaseUser;
    DatabaseReference reference;
    FirebaseFirestore firestore;
    private Handler handler = new Handler();

    private FragmentChatsBinding binding;
    private List<ChatList> list;

    private ArrayList<String> allUserId;
    private ChatListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);

        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        /*String sachinimage = "https://cdn.dnaindia.com/sites/default/files/styles/full/public/2020/03/31/900012-sachin-tendulkar.jpg";
        String rahulimage = "https://www.crictracker.com/wp-content/uploads/2015/04/Rahul-Dravid-RR-Coach.jpg";
        String kaifimage = "https://sportzwiki.com/wp-content/uploads/2018/07/Mohammad-Kaif-NatWest-Series-Final-England-India.jpg";
        String ajinkyaimage = "https://imagevars.gulfnews.com/2019/11/12/Ajinkya-Rahane-_16e5fa7dea5_medium.jpg";
        String viruimage = "https://pbs.twimg.com/profile_images/1025374083595350022/9nKlG5Nm_400x400.jpg";

        list.add(new ChatList("10", "Sachin Tendulkar", "God of Cricket", "24/04/2020", sachinimage));
        list.add(new ChatList("02", "Rahul Dravid", "The Wall", "11 January 2020", rahulimage));
        list.add(new ChatList("03", "Mohammad Kaif", "Leapard", "1 December 2020", kaifimage));
        list.add(new ChatList("04", "Ajikya Rahane", "New Rahul Dravid", "6 June 2020", ajinkyaimage));
        list.add(new ChatList("05", "Virendra Sehwag", "Boller Killer", "20 October 1978", viruimage));

        recyclerView.setAdapter(new ChatListAdapter(list, getContext()));*/

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        list = new ArrayList<>();
        allUserId = new ArrayList<>();

        binding.chatRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ChatListAdapter(list, getContext());
        binding.chatRecycler.setAdapter(new ChatListAdapter(list, getContext()));

        if (firebaseUser != null) {
            getChatsList();
        }
        return binding.getRoot();
    }

    private void getChatsList() {
        reference.child("ChatList").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list.clear();
                allUserId.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String userId = Objects.requireNonNull(snapshot.child("chatid").getValue()).toString();
                    Log.d(TAG, "onDataChange: userId" + userId);

                    //getUserData(userId);
                    allUserId.add(userId);

                }
                getUserInfo();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getUserInfo() {

        handler.post(new Runnable() {
            @Override
            public void run() {
                for (String userID : allUserId) {
                    firestore.collection("Users").document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            Log.d(TAG, "onSuccess: ddd" + documentSnapshot.getString("userName"));
                            try {
                                ChatList chat = new ChatList(
                                        documentSnapshot.getString("userID"),
                                        documentSnapshot.getString("userName"),
                                        "this is description..",
                                        "",
                                        documentSnapshot.getString("imageProfile")
                                );
                                list.add(chat);
                            } catch (Exception e) {
                                Log.d(TAG, "onSuccess: " + e.getMessage());
                            }
                            if (adapter != null) {
                                adapter.notifyItemInserted(0);
                                adapter.notifyDataSetChanged();

                                Log.d(TAG, "onSuccess: adapter " + adapter.getItemCount());
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: Error L" + e.getMessage());
                        }
                    });
                }
            }
        });
    }


    private void getUserData(final String userId) {
        firestore.collection("Users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                ChatList chatList = new ChatList(
                        documentSnapshot.getString(userId),
                        documentSnapshot.getString("userName"),
                        "Demo description",
                        "", documentSnapshot.getString("imageProfile"));
                list.add(chatList);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: userId" + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}