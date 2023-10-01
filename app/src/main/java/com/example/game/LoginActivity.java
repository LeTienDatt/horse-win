package com.example.game;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    static final String REQUIRE = "REQUIRED";
    static final String USER_KEY = "USER";
    static final String USER_MONEY = "MONEY";
    private EditText txtUsername;
    private EditText txtPassword;
    private Button btnLogin;

    // username -> (password, money)
    private HashMap<String, Pair<String, Integer>> userInfos = new HashMap<>();
    private ActivityResultLauncher<Intent> gameLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        registerLauncher();
        binding();
        setupListener();
        setupUserInfo();
    }

    protected void binding() {
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    protected void setupUserInfo() {
        userInfos.put("david", new Pair<>("1234", 1000)); // username: david, password: 1234, money: 1000$
    }

    protected void registerLauncher() {
        // waiting for result from main game
        gameLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent i = result.getData();

                    String user = i.getStringExtra(USER_KEY);
                    String password = userInfos.get(user).first;
                    int point = i.getIntExtra(USER_MONEY, -1);

                    // update existed record
                    userInfos.put(user, new Pair<>(password, point));
                }
            }
        });
    }

    protected void setupListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkLogin()) {
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                String user = txtUsername.getText().toString();
                Pair<String, Integer> p = userInfos.get(user);
                intent.putExtra(USER_KEY, user);
                intent.putExtra(USER_MONEY, p.second);
                gameLauncher.launch(intent);
            }
        });
    }

    protected boolean checkLogin() {
        String user = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        if (user.isEmpty()) {
            txtUsername.setError(REQUIRE);
            return false;
        }

        if (password.isEmpty()) {
            txtPassword.setError(REQUIRE);
            return false;
        }

        if (!userInfos.containsKey(user) || !userInfos.get(user).first.equals(password)) {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}