package com.whit.wordlesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private boolean isSignup;
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_signup);
        String message = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        Button button = (Button) findViewById(R.id.accountButton);
        email = (EditText) findViewById(R.id.emailField);
        password = (EditText) findViewById(R.id.passwordField);
        isSignup = message.contains("create");
        button.setText(isSignup ? "Create account" : "Login");
    }
    public void onSignup(View view) {
        if (isSignup) {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Account", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignupActivity.this, WordleViewActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Account", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("Account", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(SignupActivity.this, WordleViewActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("Account", "signInWithEmail:failure", task.getException());
                                Toast.makeText(SignupActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }
    }
}

