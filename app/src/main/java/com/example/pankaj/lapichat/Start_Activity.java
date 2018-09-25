package com.example.pankaj.lapichat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start_Activity extends AppCompatActivity {

    private Button regi_Button;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_);

        regi_Button = findViewById(R.id.Reg_btn);
        login_button = findViewById(R.id.login_btn);


    regi_Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Start_Activity.this,Registration_Activity.class));
        }
    });

    login_button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(Start_Activity.this,Login_Activity.class));
        }
    });
    }

}
