package com.innoreva.pedaller;

import static com.innoreva.pedaller.constents.Constents.BASE_URL;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private CycleAdapter cycleAdapter;
    private List<Cycle> cycleList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.cycleRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cycleList = new ArrayList<>();
        cycleAdapter = new CycleAdapter(cycleList);
        recyclerView.setAdapter(cycleAdapter);

        // Make API call to fetch cycle data
        fetchCycleData();

        return view;
    }

    private void fetchCycleData() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/api/cycles/available")
                .build();

        // Run the request in a background thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        // Parse the response and update UI
                        String responseData = response.body().string();
                        JSONArray jsonArray = new JSONArray(responseData);

                        // Clear the existing data
                        cycleList.clear();

                        // Populate the cycleList with data from the API response
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject cycleObject = jsonArray.getJSONObject(i);
                            String location = cycleObject.getString("location");
                            int hourlyRate = cycleObject.getInt("hourlyRate");
                            String availableTime = "Available time - 10:00 - 15:00"; // Update with real data if necessary

                            // Add the cycle to the list
                            cycleList.add(new Cycle(
                                    "Location: " + location,
                                    "Cost per hour - Rs. " + hourlyRate,
                                    availableTime,
                                    R.drawable.bicycle_image // You can replace this with dynamic image loading if needed
                            ));
                        }

                        // Notify the adapter to refresh the data
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cycleAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}
