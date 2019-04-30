package com.cmpe277.lab2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener
{
        private EditText editTextEmail;
        private EditText editTextPassword;
        private FirebaseAuth mAuth;
        private ProgressBar progresBar;
        private SignUpActivity signUpActivity;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            FirebaseApp.initializeApp(this);
            super.onCreate( savedInstanceState );
            setContentView( R.layout.activity_sign_up );

            editTextEmail = (EditText) findViewById( R.id.editTextEmail );
            editTextPassword = (EditText) findViewById( R.id.editTextPassword );
            progresBar = (ProgressBar) findViewById( R.id.progressbar );

            mAuth = FirebaseAuth.getInstance();
            findViewById( R.id.buttonSignUp ).setOnClickListener( this );
            signUpActivity = this;


        }

        private void registerUser(){
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
                return;

            }
            if(password.isEmpty()){
                editTextPassword.setError( "Password is required" );
                editTextPassword.requestFocus();
                return;
            }
            if(password.length() < 6){
                editTextPassword.setError( "Minimum length of password should be 6" );
                editTextPassword.requestFocus();
                return;
            }

            progresBar.setVisibility( View.VISIBLE );

            mAuth.createUserWithEmailAndPassword( email,password ).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progresBar.setVisibility( View.GONE );
                    if(task.isSuccessful()){
                        startActivity(new Intent(signUpActivity,LoginActivity.class));
                    } else{
                        if(task.getException() instanceof FirebaseAuthUserCollisionException){
                            Toast.makeText(getApplicationContext(),
                                    "Email already registered",
                                    Toast.LENGTH_SHORT ).show();
                        }
                        else {
                            Toast.makeText( getApplicationContext(),
                                    task.getException().getMessage(),
                                    Toast.LENGTH_SHORT ).show();
                        }
                    }
                }
            } );
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.buttonSignUp:
                    registerUser();
                    break;
                case R.id.textViewLogin:
                    startActivity( new Intent(this, LoginActivity.class));
                    break;
            }
        }
}
