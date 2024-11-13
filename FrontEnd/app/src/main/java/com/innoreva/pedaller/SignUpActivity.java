package com.innoreva.pedaller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignUpActivity extends AppCompatActivity {
    Button signup;
    EditText name, email, password, confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signup = findViewById(R.id.btn_signup);
        name = findViewById(R.id.et_name);
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        confirm_password = findViewById(R.id.et_confirm_password);

        signup.setOnClickListener(v -> {
            // Perform sign-up request
            if (validateInputs()) {
                registerUser();
            }
        });

        TextView alreadyAccount = findViewById(R.id.tv_login);
        alreadyAccount.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, MainActivity.class)));
    }

    private boolean validateInputs() {
        // Add input validation logic here (e.g., check if fields are not empty)
        if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                password.getText().toString().isEmpty() || confirm_password.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.getText().toString().equals(confirm_password.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void registerUser() {
//        Log.d("reandom", name.getText().toString() + " " + email.getText().toString() + " " + password.getText().toString() + " " + confirm_password.getText().toString());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                URL url = new URL("http://139.84.173.61:3000/api/auth/register");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Create JSON object for request body
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", name.getText().toString());
                jsonBody.put("email", email.getText().toString());
                jsonBody.put("phoneNumber", "7033167930"); // You can replace with dynamic input if needed
                jsonBody.put("password", password.getText().toString());
                jsonBody.put("role", "user");

                // Write JSON data to output stream
                OutputStream os = connection.getOutputStream();
                os.write(jsonBody.toString().getBytes(StandardCharsets.UTF_8));
                os.close();

                // Get response from the server
                int responseCode = connection.getResponseCode();
                Log.d("reandom", String.valueOf(responseCode));
                if (responseCode == HttpURLConnection.HTTP_CREATED) {
                    // Read response
//                    Log.d("reandom", );
                    Scanner scanner = new Scanner(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    while (scanner.hasNext()) {
                        response.append(scanner.nextLine());
                    }
                    scanner.close();

                    // Parse JSON response to get the token
                    JSONObject responseJson = new JSONObject(response.toString());
                    String token = responseJson.getString("token");

                    // Save token in SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("paddler", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", token);
                    editor.apply();

                    // Redirect to MainActivity2
                    runOnUiThread(() -> {
                        Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, MainActivity2.class));
                        finish();
                    });
                } else {
                    // Show error message
                    runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show());
                }
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(SignUpActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }
}
