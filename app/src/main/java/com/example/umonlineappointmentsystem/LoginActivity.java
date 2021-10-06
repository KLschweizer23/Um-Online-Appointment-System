package com.example.umonlineappointmentsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        finish();
                    }else{
                        Auth.GoogleSignInApi.signOut(googleApiClient);
                        Toast.makeText(this, "Please use your UMindanao Account", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                    finish();
                }

            }else{
                Toast.makeText(this, "Login failed!", Toast.LENGTH_SHORT).show();
            }
        }

    }
}