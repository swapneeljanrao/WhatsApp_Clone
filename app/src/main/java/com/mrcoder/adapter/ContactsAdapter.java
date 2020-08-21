package com.mrcoder.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.auth.User;
import com.mrcoder.R;
import com.mrcoder.model.users.Users;
import com.mrcoder.view.chats.ChatsActivity;

import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> {

    private List<Users> contactList;
    private Context context;

    public ContactsAdapter(List<Users> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_contact, parent, false);

        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        final Users user = contactList.get(position);

        holder.tvContactName.setText(user.getUserName());
        holder.tvDescription.setText(user.getBio());

        Glide.with(context).load(user.getImageProfile()).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userId", user.getUserId())
                        .putExtra("userName", user.getUserName())
                        .putExtra("userProfile", user.getImageProfile()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView tvContactName, tvDescription;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.contact_userImage);
            tvContactName = itemView.findViewById(R.id.tvContactName);
            tvDescription = itemView.findViewById(R.id.tvContactDescription);
        }
    }
}
