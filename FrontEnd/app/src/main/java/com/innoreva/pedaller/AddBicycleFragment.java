package com.innoreva.pedaller;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddBicycleFragment extends Fragment {

    private EditText modelname, timeslot, location, hourlyrate;
    private Button addbicycle;
    private static final String BASE_URL = "http://10.0.2.2:3000/api/cycles/add"; // use 10.0.2.2 for localhost on emulator
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public AddBicycleFragment() {
        // Required empty public constructor
    }

    public static AddBicycleFragment newInstance(String param1, String param2) {
        AddBicycleFragment fragment = new AddBicycleFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_bicycle, container, false);

        modelname = view.findViewById(R.id.editTextModelName);
        timeslot = view.findViewById(R.id.editTextTimeSlot);
        location = view.findViewById(R.id.editTextLocation);
        hourlyrate = view.findViewById(R.id.editTextHourlyRate);
        addbicycle = view.findViewById(R.id.buttonUploadImage);

        addbicycle.setOnClickListener(v -> addBicycle());

        return view;
    }

    private void addBicycle() {
        // Retrieve the auth token from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("paddler", Context.MODE_PRIVATE);
        String authToken = sharedPreferences.getString("token", null);

        if (authToken == null) {
            Toast.makeText(getActivity(), "Authorization token is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get input data
        String model = modelname.getText().toString();
        String slot = timeslot.getText().toString();
        String loc = location.getText().toString();
        String rate = hourlyrate.getText().toString();

        // Create JSON body
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("model", model);
            jsonObject.put("location", loc);
            jsonObject.put("hourlyRate", Integer.parseInt(rate));
            jsonObject.put("slot", slot);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url(BASE_URL)
                .addHeader("Authorization", "Bearer " + authToken)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("AddBicycle", "Request Failed", e);
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getActivity(), "Failed to add bicycle", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("AddBicycle", "Bicycle added successfully");
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Bicycle added successfully", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    Log.e("AddBicycle", "Failed to add bicycle: " + response.message());
                    getActivity().runOnUiThread(() ->
                            Toast.makeText(getActivity(), "Failed to add bicycle", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }
}
