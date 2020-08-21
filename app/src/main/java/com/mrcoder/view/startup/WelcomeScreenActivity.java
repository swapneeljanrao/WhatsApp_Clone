package com.mrcoder.view.startup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mrcoder.MainActivity;
import com.mrcoder.R;
import com.mrcoder.view.auth.PhoneLogin;

public class WelcomeScreenActivity extends AppCompatActivity {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        button = findViewById(R.id.btnAgree);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeScreenActivity.this, PhoneLogin.class));
                finish();
            }
        });

    }
}