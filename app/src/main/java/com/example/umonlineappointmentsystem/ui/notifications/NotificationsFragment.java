package com.example.umonlineappointmentsystem.ui.notifications;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.umonlineappointmentsystem.DatabaseManager;
import com.example.umonlineappointmentsystem.LoginActivity;
import com.example.umonlineappointmentsystem.R;
import com.example.umonlineappointmentsystem.databinding.FragmentUserBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.squareup.picasso.Picasso;

import de.codecrafters.tableview.TableView;

public class NotificationsFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener {

    private NotificationsViewModel notificationsViewModel;
    private FragmentUserBinding binding;

    private View fragmentView;

    private TextView tv_email, tv_accountType, tv_reminder;
    private ImageView img_user;
    private ImageButton btn_logout;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    private Context thisContext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        fragmentView = inflater.inflate(R.layout.fragment_user, container, false);

        binding = FragmentUserBinding.inflate(inflater, container, false);

        thisContext = getActivity().getApplicationContext();

        initializeComponents();
        logoutFunction();

        return fragmentView;
    }
    private void initializeComponents(){
        tv_email = fragmentView.findViewById(R.id.tv_email);
        tv_accountType = fragmentView.findViewById(R.id.tv_accountTypeUser);
        img_user = fragmentView.findViewById(R.id.img_user);
        btn_logout = fragmentView.findViewById(R.id.btn_logout);
        tv_reminder = fragmentView.findViewById(R.id.tv_reminder);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(thisContext).enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
    }

    private void logoutFunction(){
        btn_logout.setOnClickListener(view -> {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(status -> {
                if(status.isSuccess()){
                    goToLoginActivity();
                }else{
                    Toast.makeText(thisContext, "Logout Failed!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void goToLoginActivity() {
        startActivity(new Intent(thisContext, LoginActivity.class));
        getActivity().finish();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String accountEmail = account.getEmail();
            String finalEmail = accountEmail.length() > 15 ? accountEmail.substring(0, 15) + "..." : accountEmail;
            tv_email.setText(finalEmail);

            final TableView<String[]> tableView = (TableView<String[]>) fragmentView.findViewById(R.id.tableView_user);

            if(account.getEmail().contains("umindanao.edu")){
                tv_accountType.setText("Permanent Account!");
                tv_reminder.setText("");

                DatabaseManager dm = new DatabaseManager("appointments");
                dm.setTableDataForUser(tableView, account, getActivity());
            }else{
                tv_accountType.setText("Temporary Account!");
                ((ViewGroup)fragmentView.getParent()).removeView(tableView);
            }

            Picasso.get().load(account.getPhotoUrl()).placeholder(R.drawable.user_profile).into(img_user);
        }else{
            goToLoginActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(googleSignInResult -> {
                handleSignInResult(googleSignInResult);
            });
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(getActivity());
        googleApiClient.disconnect();
    }
}