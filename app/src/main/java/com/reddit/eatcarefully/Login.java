package com.reddit.eatcarefully;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

public class Login extends AppCompatActivity {

    TextInputLayout User, Pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        User = findViewById(R.id.Username);
        Pass = findViewById(R.id.Password);
    }


    public void login(View view) {
        String Username, Password;

        Username= Objects.requireNonNull(User.getEditText()).getText().toString();
        Password= Objects.requireNonNull(Pass.getEditText()).getText().toString();

        if(Username.equals("admin")&& Password.equals("admin"))
        {
            Intent intent= new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        else
            Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();

    }
}
