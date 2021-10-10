package com.example.umonlineappointmentsystem.ui.dashboard;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.umonlineappointmentsystem.AppointmentObject;
import com.example.umonlineappointmentsystem.DashboardActivity;
import com.example.umonlineappointmentsystem.R;
import com.example.umonlineappointmentsystem.databinding.FragmentDashboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;

    private View fragmentView;

    private TextView tv_date;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        processDate();
        processData();

        return fragmentView;
    }

    private void processDate(){
        tv_date = (TextView) fragmentView.findViewById(R.id.tv_date);

        tv_date.setOnClickListener(view -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    year,month,day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

            }
        };
    }
    private void processData(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("appointments");

        ref.child("2021-10-08").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<AppointmentObject> aoList = new ArrayList<>();
                String[] appointmentHeaders = {"#", "Name", "Purpose"};
                String[][] appointments;

                for(DataSnapshot ds : snapshot.getChildren())
                {
                    AppointmentObject ao = ds.getValue(AppointmentObject.class);
                    aoList.add(ao);
                }

                appointments = new String[aoList.size()][3];

                for(int i = 0; aoList.size() > i; i++) {
                    AppointmentObject ao = aoList.get(i);

                    appointments[i][0] = i + 1 + "";
                    appointments[i][1] = ao.getName();
                    appointments[i][2] = ao.getPurpose();
                }
                final TableView<String[]> tableView = (TableView<String[]>) fragmentView.findViewById(R.id.tableView);
                tableView.setColumnCount(3);
                tableView.setHeaderBackgroundColor(Color.parseColor("#880000"));

                SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(getActivity(), appointmentHeaders);
                headerAdapter.setTextColor(Color.parseColor("#ffffff"));

                tableView.setHeaderAdapter(headerAdapter);
                tableView.setDataAdapter(new SimpleTableDataAdapter(getActivity(), appointments));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}