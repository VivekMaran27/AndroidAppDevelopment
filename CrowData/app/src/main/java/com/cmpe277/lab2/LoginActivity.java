package com.cmpe277.lab2;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static FirebaseAuth mAuth;
    private static EditText editTextEmail;
    private static EditText editTextPassword;
    private static Button buttonLogin;
    private static final String TAG = "LoginActivity";
    private static TextView register;
    private ProgressBar progresBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetupActivity();
        mAuth = FirebaseAuth.getInstance();
        findViewById( R.id.textViewSignUp ).setOnClickListener( this );
        findViewById( R.id.buttonLogin ).setOnClickListener( this );
    }


    /**
     * Sets up the login activity by registering an oncclick listener for
     * login button
     */
    private void SetupActivity() {
        editTextEmail = (EditText) findViewById(R.id.editTextEmail );
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        register = (TextView) findViewById( R.id.textViewSignUp ) ;
        progresBar = (ProgressBar) findViewById( R.id.progressbar );
    }

    private void onLoginClicked(){

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError( "Email is required" );
            editTextEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher( email ).matches()){
            editTextEmail.setError( "Enter valid email" );
            editTextEmail.requestFocus();

        }
        if(password.isEmpty()){
            editTextPassword.setError( "Password is required" );
            editTextPassword.requestFocus();
        }
        if(password.length() < 6){
            editTextPassword.setError( "Minimum length of password should be 6" );
            editTextPassword.requestFocus();
        }

        progresBar.setVisibility( View.VISIBLE );
        mAuth.signInWithEmailAndPassword(email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progresBar.setVisibility( View.GONE );
                if(task.isSuccessful()){
                    // Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    Intent intent = new Intent("android.intent.action" + ".POSTS");
                    intent.putExtra("User", editTextEmail.getText().toString());
                    intent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    Log.d(TAG, "Starting POSTS intent");
                    startActivity(intent);

                    // startActivity( intent );

                }else{
                    Toast.makeText(getApplicationContext(),task.getException()
                            .getMessage(),Toast.LENGTH_SHORT).show();
                }

            }
        } );
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.buttonLogin:
                onLoginClicked();
                break;
            case R.id.textViewSignUp:
                startActivity(new Intent (this, SignUpActivity.class));
                break;
        }

    }
}