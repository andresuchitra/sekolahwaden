package com.andresuchitra.sekolahku;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etUserName,etPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Casting View

        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        Button btnLogin = findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUserName.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if(username.isEmpty())
                {
                    etUserName.setError("UserName can't be EMPTY!");
                    etUserName.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    etPassword.setError("Password can't be EMPTY!");
                    etPassword.requestFocus();
                    return;
                }
                else
                {
                    if(username.equals("admin") && password.equals("admin")){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else if(!username.equals("admin"))
                    {
                        Toast.makeText(LoginActivity.this, "User Name is wrong!", Toast.LENGTH_SHORT).show();
                    }
                    else if(!password.equals("admin")){
                        Toast.makeText(LoginActivity.this, "Password is wrong!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}
