package com.innoreva.pedaller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProfileFragment extends Fragment {

    private TextView name, phone, email, dob, member_since;
    private OkHttpClient client;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = new OkHttpClient(); // Initialize OkHttpClient
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.tv_name);
        phone = view.findViewById(R.id.tv_phone);
        email = view.findViewById(R.id.tv_email);
        dob = view.findViewById(R.id.tv_dob); // If DOB is part of the profile
        member_since = view.findViewById(R.id.tv_member_since);

        fetchProfileData();

        return view;
    }

    private void fetchProfileData() {
        String url = "http://139.84.173.61:3000/api/profile/data";
        Request request = new Request.Builder()
                .url(url)
                .header("Authorization", "Bearer " + getToken()) // Replace with the actual token
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                // Handle the error appropriately (e.g., show a message to the user)
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        // Update the UI with the profile data
                        getActivity().runOnUiThread(() -> updateUI(json));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void updateUI(JSONObject json) {
        try {
            name.setText(json.getString("userName"));
            phone.setText(json.getString("phoneNumber"));
            email.setText(json.getString("email"));
            member_since.setText(json.getString("createdAt").split("T")[0]); // Format the date as needed
            // If there are other fields like dob, handle them here as well
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getToken() {
        // Retrieve token from SharedPreferences
        return getActivity()
                .getSharedPreferences("paddler", getActivity().MODE_PRIVATE)
                .getString("token", "");
    }
}
