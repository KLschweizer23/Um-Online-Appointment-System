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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.umonlineappointmentsystem.AppointmentObject;
import com.example.umonlineappointmentsystem.DatabaseManager;
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

    private String selectedDate;

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

        tv_date = fragmentView.findViewById(R.id.tv_date);

        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] days = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        tv_date.setText(months[month] + " " + days[day - 1] + ", " + year);
        selectedDate = year + "-" + days[month] + "-" + days[day - 1];

        tv_date.setOnClickListener(view -> {
            int newYear = Integer.parseInt(selectedDate.substring(0,4));
            int newMonth = Integer.parseInt(selectedDate.substring(5,7));
            int newDay = Integer.parseInt(selectedDate.substring(8));

            DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    dateSetListener,
                    newYear,--newMonth,newDay);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                tv_date.setText(months[month] + " " + days[day - 1] + ", " + year);
                selectedDate = year + "-" + days[month] + "-" + days[day - 1];
                processData();
            }
        };
    }
    private void processData(){
        final TableView<String[]> tableView = (TableView<String[]>) fragmentView.findViewById(R.id.tableView);
        DatabaseManager dm = new DatabaseManager("appointments");
        dm.setTableData(tableView, selectedDate, getActivity(), false, null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}