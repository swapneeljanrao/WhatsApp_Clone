package com.mrcoder.menu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrcoder.R;
import com.mrcoder.adapter.CallListAdapter;
import com.mrcoder.model.CallList;

import java.util.ArrayList;
import java.util.List;

public class CallsFragment extends Fragment {

    public CallsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calls, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.callsRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<CallList> lists = new ArrayList<>();

       /* String sachinimage = "https://cdn.dnaindia.com/sites/default/files/styles/full/public/2020/03/31/900012-sachin-tendulkar.jpg";

        lists.add(new CallList("01", "Sachin Tendulkar", sachinimage, "", "out"));
        lists.add(new CallList("02", "Sachin Tendulkar", sachinimage, "", "in"));
        lists.add(new CallList("03", "Sachin Tendulkar", sachinimage, "", "missed"));
        recyclerView.setAdapter(new CallListAdapter(lists, getContext()));*/
        return view;

    }
}