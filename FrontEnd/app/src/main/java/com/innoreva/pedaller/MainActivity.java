package com.innoreva.pedaller;

//package com.innoreva.pedaller;

import static com.innoreva.pedaller.constents.Constents.BASE_URL;

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

// Import for OkHttp (if switching to OkHttp for network requests)
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

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
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(BASE_URL+"/api/profile/details") // Your server URL
                .addHeader("Authorization", "Bearer " + token)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Network error, please try again.", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Token is valid
                    String userData = response.body().string();

                    // Save token and user data in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("paddler", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userData", userData);
                    editor.apply();

                    // Navigate to MainActivity2
                    runOnUiThread(() -> {
                        startActivity(new Intent(MainActivity.this, MainActivity2.class));
                        finish();
                    });
                } else {
                    // Invalid token, show login prompt
                    runOnUiThread(() ->
                            Toast.makeText(MainActivity.this, "Please log in to continue.", Toast.LENGTH_SHORT).show()
                    );
                }
            }
        });
    }


}
