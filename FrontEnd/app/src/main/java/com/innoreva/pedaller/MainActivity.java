package com.innoreva.pedaller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private Button signup, loginbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SharedPreferences sharedPreferences = getSharedPreferences("paddler", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        if (token != null) {
            // Validate the token with the server
            validateTokenWithServer(token);
        }

        loginbutton = findViewById(R.id.login_button);
        signup = findViewById(R.id.signup_button);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
        loginbutton.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
    }

    private void validateTokenWithServer(String token) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            boolean isValidToken = false;
            String userData = null; // To store the response data
            try {
                URL url = new URL("http://139.84.173.61:3000/api/profile/data"); // Replace with your server URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", "Bearer " + token);
                connection.connect();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // If response is OK, token is valid
                    isValidToken = true;

                    // Read the response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseBuilder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        responseBuilder.append(line);
                    }
                    userData = responseBuilder.toString(); // JSON or string data from server
                    reader.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean finalIsValidToken = isValidToken;
            String finalUserData = userData;
            handler.post(() -> {
                if (finalIsValidToken) {
                    // Save token and user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("paddler", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userData", finalUserData); // Save user data
                    editor.apply();

                    // Navigate to MainActivity2
                    startActivity(new Intent(MainActivity.this, MainActivity2.class));
                    finish();
                } else {
                    // Token is invalid or server is unreachable; show a message if needed
                    Toast.makeText(MainActivity.this, "Please log in to continue.", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
