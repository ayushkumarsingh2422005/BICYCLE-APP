package com.innoreva.pedaller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class LendFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView recyclerView;
    private BicycleAdapter bicycleAdapter;
    private List<Bicycle> bicycleList;
    private FloatingActionButton fab;

    private String mParam1;
    private String mParam2;

    public LendFragment() {
        // Required empty public constructor
    }

    public static LendFragment newInstance(String param1, String param2) {
        LendFragment fragment = new LendFragment();
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
        View view = inflater.inflate(R.layout.fragment_lend, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize Floating Action Button
        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new AddBicycleFragment())
                    .commit();
        });

        // Initialize list and adapter
        bicycleList = new ArrayList<>();
        bicycleAdapter = new BicycleAdapter(getActivity(),bicycleList);
        recyclerView.setAdapter(bicycleAdapter);

        // Populate list with sample data
        loadBicycles();

        return view;
    }
    private void loadBicycles() {
        // Add sample data or fetch from database
        bicycleList.add(new Bicycle("Near Nescafe", "Exact location description", 50, "Name XYZ", "12:00 to 17:00 (03/11/24)", R.drawable.bicycle_image));
        bicycleList.add(new Bicycle("Library Area", "Near entrance", 40, "Name ABC", "10:00 to 18:00 (03/11/24)", R.drawable.bicycle_image));
        // Notify adapter of data change
        bicycleAdapter.notifyDataSetChanged();
    }
}