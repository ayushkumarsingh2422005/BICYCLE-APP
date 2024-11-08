package com.innoreva.pedaller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CycleAdapter cycleAdapter;
    private List<Cycle> cycleList;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.cycleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cycleList = new ArrayList<>();
        cycleList.add(new Cycle("Near Nescafe", "Cost per hour - Rs. 50", "Available time - 12:00 - 17:00", R.drawable.bicycle_image));
        cycleList.add(new Cycle("Near Library", "Cost per hour - Rs. 40", "Available time - 10:00 - 15:00", R.drawable.bicycle_image));
        cycleList.add(new Cycle("Near Hostel", "Cost per hour - Rs. 60", "Available time - 14:00 - 20:00", R.drawable.bicycle_image));
         cycleList.add(new Cycle("Near Nescafe", "Cost per hour - Rs. 50", "Available time - 12:00 - 17:00", R.drawable.bicycle_image));
        cycleList.add(new Cycle("Near Library", "Cost per hour - Rs. 40", "Available time - 10:00 - 15:00", R.drawable.bicycle_image));
        cycleList.add(new Cycle("Near Hostel", "Cost per hour - Rs. 60", "Available time - 14:00 - 20:00", R.drawable.bicycle_image));
         cycleList.add(new Cycle("Near Nescafe", "Cost per hour - Rs. 50", "Available time - 12:00 - 17:00", R.drawable.bicycle_image));
        cycleList.add(new Cycle("Near Library", "Cost per hour - Rs. 40", "Available time - 10:00 - 15:00", R.drawable.bicycle_image));
        cycleList.add(new Cycle("Near Hostel", "Cost per hour - Rs. 60", "Available time - 14:00 - 20:00", R.drawable.bicycle_image));

        // Add more items as needed

        cycleAdapter = new CycleAdapter(cycleList);
        recyclerView.setAdapter(cycleAdapter);
        return view;
    }
}