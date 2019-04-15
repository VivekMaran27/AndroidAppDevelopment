package com.cmpe277.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static Button login_button;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetupActivity();
    }

    /**
     * Sets up the login activity by registering an oncclick listener for
     * login button
     */
    private void SetupActivity() {
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        login_button = (Button) findViewById(R.id.button);
        Log.i(TAG, "SetupActivity++");
        login_button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG, "Login onClick++");
                        if (username.getText().toString().equals("user") &&
                                password.getText().toString().equals("pass")) {
                            Toast.makeText(LoginActivity.this,
                                    "Username and " +
                                            "password" +
                                            " is correct",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("android.intent.action" +
                                    ".POSTS");
                            intent.putExtra("User",
                                    username.getText().toString());
                            Log.d(TAG, "Starting POSTS intent");
                            startActivity(intent);
                        } else {
                            Log.d(TAG, "Password invalid " + "Expected: " +
                                    "pass" + "Typed: "+password.getText().toString());
                            Toast.makeText(LoginActivity.this, "" +
                                            "Username or " +
                                            "password" +
                                            " is NOT correct",
                                    Toast.LENGTH_SHORT).show();
                        }
                        Log.i(TAG, "Login onClick--");
                    }
                }
        );
        Log.i(TAG, "SetupActivity--");
    }
}
