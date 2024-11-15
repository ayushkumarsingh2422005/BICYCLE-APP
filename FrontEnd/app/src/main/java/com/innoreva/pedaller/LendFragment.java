package com.innoreva.pedaller;

import static com.innoreva.pedaller.constents.Constents.BASE_URL;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LendFragment extends Fragment {

    private RecyclerView recyclerView;
    private BicycleAdapter bicycleAdapter;
    private List<Bicycle> bicycleList;
    private FloatingActionButton fab;

    public LendFragment() {
        // Required empty public constructor
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
        bicycleAdapter = new BicycleAdapter(getActivity(), bicycleList);
        recyclerView.setAdapter(bicycleAdapter);

        // Load bicycles with authentication
        loadBicycles();

        return view;
    }

    private void loadBicycles() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("paddler", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token == null) {
            Toast.makeText(getContext(), "Please log in to view bicycles.", Toast.LENGTH_SHORT).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL(BASE_URL+"/api/cycles/available"); // Replace with your API endpoint
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    parseBicycleData(response.toString());
                } else {
                    showError("Failed to load bicycles. Please try again.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("An error occurred while fetching bicycles.");
            }
        });
    }

    private void parseBicycleData(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            bicycleList.clear(); // Clear previous data if any
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bicycleObj = jsonArray.getJSONObject(i);

                String model = bicycleObj.getString("model");
                String location = bicycleObj.getString("location");
                int hourlyRate = bicycleObj.getInt("hourlyRate");
                String ownerName = bicycleObj.getJSONObject("ownerId").getString("userName");
                String availableTime = "Available"; // You can adjust this based on your needs

                // Add new Bicycle object to the list
                bicycleList.add(new Bicycle(model, location, hourlyRate, ownerName, availableTime, R.drawable.bicycle_image));
            }

            requireActivity().runOnUiThread(() -> bicycleAdapter.notifyDataSetChanged());
        } catch (JSONException e) {
            e.printStackTrace();
            showError("Failed to parse bicycle data.");
        }
    }

    private void showError(String message) {
        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show());
    }
}
