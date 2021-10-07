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

import com.example.umonlineappointmentsystem.R;
import com.example.umonlineappointmentsystem.databinding.FragmentFormBinding;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentFormBinding binding;
    private View fragmentView;
    private Context thisContext;
    private TextInputEditText et_name,et_email,et_yearCourse;
    private TextInputLayout til_name,til_email,til_yearCourse;
    private  Spinner drp_purpose;
    private CheckBox cb_agreement;
    private Button button2;


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
                      Toast.makeText(thisContext, "Appointment form submitted!", Toast.LENGTH_SHORT).show();
                  }
              }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}