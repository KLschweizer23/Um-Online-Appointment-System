package com.example.umonlineappointmentsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private GoogleApiClient googleApiClient;
    private static final int SIGN_IN = 1;

    private boolean mustBeUMindanao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(view -> {
            mustBeUMindanao = true;
            Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(i, SIGN_IN);
        });
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setOnClickListener(view -> {
            mustBeUMindanao = false;
            Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(i, SIGN_IN);
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                if(mustBeUMindanao){
                    if(account.getEmail().contains("umindanao.edu")) {
                        processDatabase(account);
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }else{
                        Auth.GoogleSignInApi.signOut(googleApiClient);
                        Toast.makeText(this, "Please use your UMindanao Account", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    processDatabase(account);
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                }
            }else{
                Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void processDatabase(GoogleSignInAccount account){
        FirebaseDatabase rootNode;
        DatabaseReference reference;

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("accounts");


        String id = account.getId();
        String displayName = account.getDisplayName();
        String email = account.getEmail();
        String expiration = setExpiration(account);
        String umindanaoAccount = (account.getEmail().contains("umindanao.edu") ? "Permanent " : "Temporary ") + "Account!" ;

        AccountObject ao = new AccountObject(id, displayName, email, expiration, umindanaoAccount);

        reference.orderByChild("accounts").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                }else{

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child(id).setValue(ao);
    }
    private String setExpiration(GoogleSignInAccount account){
        String returnValue = "";
        if(account.getEmail().contains("umindanao.edu")){
            returnValue = "0";
        }else{
            LocalDate today = LocalDate.now();
            LocalDate expirationDay = today.plusDays(7);
            returnValue = expirationDay.toString();
        }

        return returnValue;
    }
}