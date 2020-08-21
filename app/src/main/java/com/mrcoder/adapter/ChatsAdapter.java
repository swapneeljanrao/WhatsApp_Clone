package com.mrcoder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mrcoder.R;
import com.mrcoder.model.chat.Chats;

import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyChatViewHolder> {

    private List<Chats> list;
    private Context context;
    public static final int MESSAGE_TYPE_LEFT = 0;
    public static final int MESSAGE_TYPE_RIGHT = 1;
    FirebaseUser firebaseUser;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public MyChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MESSAGE_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new MyChatViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new MyChatViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyChatViewHolder holder, int position) {

        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyChatViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTextMessage;

        public MyChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTextMessage = itemView.findViewById(R.id.tvChatMessage);
        }

        void bind(Chats chats) {
            tvTextMessage.setText(chats.getTextMessage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(firebaseUser.getUid())) {
            return MESSAGE_TYPE_RIGHT;
        } else {
            return MESSAGE_TYPE_LEFT;
        }
    }
}
