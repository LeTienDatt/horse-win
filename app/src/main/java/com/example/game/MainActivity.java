package com.example.game;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView txtUsername;
    private TextView txtMoney;
    private Button btnLogout;
    private String username;
    private int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        binding();
        setListeners();
    }

    protected void binding() {
        txtUsername = findViewById(R.id.txtUser);
        txtMoney = findViewById(R.id.txtMoney);
        btnLogout = findViewById(R.id.btnLogout);

        // get username and money from Login Activity
        Intent intent = getIntent();
        username = intent.getStringExtra(LoginActivity.USER_KEY);
        money = intent.getIntExtra(LoginActivity.USER_MONEY, -1);

        txtUsername.setText(String.format("Welcome %s", username));
        txtMoney.setText(String.format("Balance %d$", money));
    }

    protected void setListeners() {
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra(LoginActivity.USER_KEY, username);
                intent.putExtra(LoginActivity.USER_MONEY, money);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}