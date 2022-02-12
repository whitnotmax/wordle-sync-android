package com.whit.wordlesync;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class WordleViewActivity extends AppCompatActivity {

    private String getWordleStatsScript;
    private String syncScript;
    private WebView webView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wordle_view);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        webView = (WebView) findViewById(R.id.webview);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Button syncScores = (Button) findViewById(R.id.syncScoresButton);
        Button saveScores = (Button) findViewById(R.id.saveScoresButton);
        TextView warning = (TextView) findViewById(R.id.warningText);
        WebView.setWebContentsDebuggingEnabled(true);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        //This the the enabling of the zoom controls
        webSettings.setBuiltInZoomControls(true);

        //This will zoom out the WebView
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);





        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.INVISIBLE);
                syncScores.setVisibility(View.VISIBLE);
                saveScores.setVisibility(View.VISIBLE);
                warning.setVisibility(View.VISIBLE);



                DocumentReference docRef = db.collection("users").document(mAuth.getCurrentUser().getUid());
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                data = document.getData().get("data").toString();
                                getWordleStatsScript = "return localStorage[\\'nyt-wordle-statistics\\']";
                                syncScript =

                                        "var stats = " + data + ";" +
                                                "localStorage['nyt-wordle-statistics'] = \"\";"+
                                                "console.log(stats);" +
                                                "var str = JSON.stringify(stats);" +
                                                "console.log(str);" +
                                                "localStorage['nyt-wordle-statistics'] = stats;";


                                Log.d("AboutToRun", String.format("Function(\"%s\")();", syncScript));
                                webView.evaluateJavascript(syncScript, new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String value) {

                                        Toast.makeText(WordleViewActivity.this, "Sync success!", Toast.LENGTH_SHORT).show();
                                    }
                                });




                            } else {
                                Log.d("firestore", "No such document");
                                Toast.makeText(WordleViewActivity.this, "You have no data to sync! Play a game and sync or sync your data from your computer.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("firestore", "get failed with ", task.getException());
                        }
                    }
                });
            }
        });

        webView.loadUrl("https://powerlanguage.co.uk/wordle/");


    }


    public void onLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void onSync(View view) {


        Log.d("AboutToRun", String.format("Function('%s')();", getWordleStatsScript));
                webView.evaluateJavascript(String.format("Function('%s')();", getWordleStatsScript), new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Map map = new HashMap();
                        map.put("data", value);
                        db.collection("users").document(mAuth.getCurrentUser().getUid())
                                .set(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("firestore", "DocumentSnapshot successfully written!");
                                        Toast.makeText(WordleViewActivity.this, "Synced!", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("firestore", "Error writing document", e);
                                        Toast.makeText(WordleViewActivity.this, "Could not sync!", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                });



    }



}