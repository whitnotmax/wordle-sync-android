package com.whit.wordlesync;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.whit.wordlesync.ACCOUNT_MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(this, WordleViewActivity.class);
            startActivity(intent);
        }
    }

    public void sendMessage(View view) {

        Intent accountIntent = new Intent(this, SignupActivity.class);
        Button button = (Button) view;
        accountIntent.putExtra(EXTRA_MESSAGE, button.getText().toString().toLowerCase(Locale.ROOT));
        startActivity(accountIntent);

    }
}