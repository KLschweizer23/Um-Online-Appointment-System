package com.example.umonlineappointmentsystem.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.umonlineappointmentsystem.AppointmentObject;
import com.example.umonlineappointmentsystem.DatabaseManager;
import com.example.umonlineappointmentsystem.R;
import com.example.umonlineappointmentsystem.databinding.FragmentFormBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    private HomeViewModel homeViewModel;
    private FragmentFormBinding binding;
    private View fragmentView;
    private Context thisContext;
    private TextInputEditText et_name,et_email,et_yearCourse;
    private TextInputLayout til_name,til_email,til_yearCourse;
    private  Spinner drp_purpose;
    private CheckBox cb_agreement;
    private Button button2;
    private GoogleSignInAccount account;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentFormBinding.inflate(inflater, container, false);
       fragmentView = inflater.inflate(R.layout.fragment_form,container,false);
       thisContext = getActivity().getApplicationContext();

       til_name =(TextInputLayout) fragmentView.findViewById(R.id.et_name);
       et_name =(TextInputEditText) til_name.getEditText();
       til_email = (TextInputLayout) fragmentView.findViewById(R.id.et_email);
       et_email = (TextInputEditText) til_email.getEditText();
       til_yearCourse = (TextInputLayout) fragmentView.findViewById(R.id.et_yearCourse);
       et_yearCourse = (TextInputEditText) til_yearCourse.getEditText();
       drp_purpose = fragmentView.findViewById(R.id.drp_purpose);
       cb_agreement = fragmentView.findViewById(R.id.cb_agreement);
       button2 = fragmentView.findViewById(R.id.button2);

       appointmentMethod();

       return fragmentView;

    }

        private void appointmentMethod(){

            button2.setOnClickListener(new View.OnClickListener() {
              @Override
            public void onClick(View view) {

                  if (et_name.getText().toString().isEmpty()
                          || et_email.getText().toString().isEmpty()
                          || et_yearCourse.getText().toString().isEmpty()
                          || !cb_agreement.isChecked()){
                      Toast.makeText(thisContext, "Please fill up the form!", Toast.LENGTH_SHORT).show();
                  }
                  else {
                      String id = account.getId();
                      String date = new DatabaseManager("appointments").getCurrentDate(0);
                      String name = et_name.getText().toString();
                      String email = et_email.getText().toString();
                      String yearCourse = et_yearCourse.getText().toString();
                      String purpose = drp_purpose.getSelectedItem().toString();

                      AppointmentObject ao = new AppointmentObject(id, name, email, purpose, yearCourse, date);
                      DatabaseManager dm = new DatabaseManager("appointments");
                      dm.submitForm(account, ao);
                      Toast.makeText(thisContext, "Appointment form submitted!", Toast.LENGTH_SHORT).show();
                  }
              }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(thisContext).enableAutoManage(getActivity(), this)

                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        GoogleSignInResult result = opr.get();
        if(opr.isDone()){

                account = result.getSignInAccount();
        }else{
            opr.setResultCallback(googleSignInResult -> {
                account = result.getSignInAccount();
            });
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}