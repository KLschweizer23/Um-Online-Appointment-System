package com.example.umonlineappointmentsystem.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.umonlineappointmentsystem.R;
import com.example.umonlineappointmentsystem.databinding.FragmentFormBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentFormBinding binding;
    private View fragmentView;
    private Context thisContext;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentFormBinding.inflate(inflater, container, false);
       fragmentView = inflater.inflate(R.layout.fragment_user,container,false);
       thisContext = getActivity().getApplicationContext();
       appointmentMethod();

       return fragmentView;

    }
    private void appointmentMethod(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}