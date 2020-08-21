package com.mrcoder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mrcoder.R;
import com.mrcoder.model.CallList;

import java.util.ArrayList;
import java.util.List;

public class CallListAdapter extends RecyclerView.Adapter<CallListAdapter.CallList_ViewHolder> {
    List<CallList> list;
    Context context;

    public CallListAdapter(List<CallList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CallList_ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.calls_list_layout, viewGroup, false);

        return new CallList_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallList_ViewHolder holder, int position) {

        CallList callList = list.get(position);

        Glide.with(context).load(callList.getUserProfileUrl()).into(holder.ivUserImage);
        holder.tvUserName.setText(callList.getUserName());
        holder.tvCallTiming.setText(callList.getDate());

        if (callList.getCallType().equals("in")) {
            holder.ivCallType.setImageDrawable(context.getDrawable(R.drawable.ic_call_received));
        } else if (callList.getCallType().equals("out")) {
            holder.ivCallType.setImageDrawable(context.getDrawable(R.drawable.ic_call_made));
        } else {
            holder.ivCallType.setImageDrawable(context.getDrawable(R.drawable.ic_call_missed));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CallList_ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvCallTiming;
        ImageView ivCallType, ivCall, ivUserImage;

        public CallList_ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCallTiming = itemView.findViewById(R.id.calltiming);
            tvUserName = itemView.findViewById(R.id.callPersonaName);
            ivCall = itemView.findViewById(R.id.imageCall);
            ivCallType = itemView.findViewById(R.id.ivCallType);
            ivUserImage = itemView.findViewById(R.id.callImageview);

        }
    }
}
