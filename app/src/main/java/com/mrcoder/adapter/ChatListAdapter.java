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
import com.mrcoder.R;
import com.mrcoder.model.ChatList;
import com.mrcoder.view.chats.ChatsActivity;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    private List<ChatList> list;
    private Context context;

    public ChatListAdapter(List<ChatList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_list_layout, viewgroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final ChatList chatList = list.get(position);

        holder.tvUserName.setText(chatList.getUserName());
        holder.tvDescription.setText(chatList.getDescription());
        holder.textViewDate.setText(chatList.getDate());

        Glide.with(context).load(chatList.getUserProfileUrl()).into(holder.ivUserImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, ChatsActivity.class)
                        .putExtra("userId", chatList.getUserId())
                        .putExtra("userName", chatList.getUserName())
                        .putExtra("userProfile", chatList.getUserProfileUrl()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvDescription, textViewDate;
        ImageView ivUserImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvUserName = itemView.findViewById(R.id.chatPersonaName);
            tvDescription = itemView.findViewById(R.id.chatDescription);
            textViewDate = itemView.findViewById(R.id.date);
            ivUserImage = itemView.findViewById(R.id.chatImageview);
        }
    }
}
