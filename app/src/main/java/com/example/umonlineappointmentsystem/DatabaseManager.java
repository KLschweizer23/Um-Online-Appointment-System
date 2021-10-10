package com.example.umonlineappointmentsystem;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class DatabaseManager {

    private FirebaseDatabase rootNode;
    private DatabaseReference reference;

    private String referenceName;

    private final String accounts = "accounts";
    private final String appointments = "appointments";

    private long perDateListCount = 0;

    public DatabaseManager(String referenceName) {
        this.referenceName = referenceName;
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference(referenceName);
    }

    public void addAccount(AccountObject ao){
        if(referenceName.equals(accounts)){
            reference.child(ao.getId()).setValue(ao);
        }else{
            System.err.print("Wrong reference Name!");
            System.exit(0);
        }
    }

    public void submitForm(GoogleSignInAccount account, AppointmentObject ao){
        if(referenceName.equals(appointments)){
            int plusDays = 2;
            do{
                plusDays++;
                reference.child(getCurrentDate(plusDays)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue() != null){
                            perDateListCount = snapshot.getChildrenCount();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }while(perDateListCount >= 500);
            reference.child(getCurrentDate(plusDays)).child(account.getId()).setValue(ao);
        }else{
            System.err.print("Wrong reference Name!");
            System.exit(0);
        }
    }

    public void setTableDataForUser(TableView tableView, GoogleSignInAccount account, Context passedContext){
        if(referenceName.equals("appointments") && account.getEmail().contains("umindanao.edu"))
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds : snapshot.getChildren())
                        setTableData(tableView, ds.getKey(), passedContext, true, account);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public void setTableData(TableView<String[]> tableView, String selectedDate, Context passedContext, boolean getUser, GoogleSignInAccount account){
        if(referenceName.equals("appointments"))
            reference.child(selectedDate).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<AppointmentObject> aoList = new ArrayList<>();
                    String[] appointmentHeaders = getUser ? new String[]{"#", "Where", "Purpose"} : new String[]{"#", "Name", "Purpose"};
                    String[][] appointments;

                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        if(!getUser) {
                            AppointmentObject ao = ds.getValue(AppointmentObject.class);
                            aoList.add(ao);
                        }else{
                            if(ds.getKey().equals(account.getId()))
                            {
                                AppointmentObject ao = ds.getValue(AppointmentObject.class);
                                aoList.add(ao);
                            }
                        }
                    }

                    appointments = new String[aoList.size()][3];

                    for(int i = 0; aoList.size() > i; i++) {
                        AppointmentObject ao = aoList.get(i);

                        appointments[i][0] = i + 1 + "";
                        appointments[i][1] = ao.getName();
                        appointments[i][2] = ao.getPurpose();
                    }
                    tableView.setColumnCount(3);
                    tableView.setHeaderBackgroundColor(Color.parseColor("#880000"));

                    SimpleTableHeaderAdapter headerAdapter = new SimpleTableHeaderAdapter(passedContext, appointmentHeaders);
                    headerAdapter.setTextColor(Color.parseColor("#ffffff"));

                    tableView.setHeaderAdapter(headerAdapter);
                    tableView.setDataAdapter(new SimpleTableDataAdapter(passedContext, appointments));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    public String getCurrentDate(int plusDays){
        LocalDate currentDate = LocalDate.now().plusDays(plusDays);
        return currentDate.toString();
    }

    public int getTodayInterval(String expoDate){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        String endDate = expoDate;

        DateTimeZone PHILIPPINES = DateTimeZone.forID(("Philippines"));
        DateTime start = new DateTime(year, month, day, 0, 0, PHILIPPINES);
        DateTime end = new DateTime(Integer.parseInt(endDate.substring(0,4)), Integer.parseInt(endDate.substring(5,7)), Integer.parseInt(endDate.substring(8)), 0 ,0, PHILIPPINES);
        int days = Days.daysBetween(start.toLocalDate(), end.toLocalDate()).getDays();
        return days;
    }

    public void setCurrentAccount(TextView tv_accountType, GoogleSignInAccount account){

        if(referenceName.equals("appointments"))
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for(DataSnapshot ds : snapshot.getChildren())
                    {
                        if(ds.getKey().equals(account.getId()))
                        {
                            AccountObject ao = ds.getValue(AccountObject.class);
                            if(ao.getEmail().contains("umindanao.edu")){
                                tv_accountType.setText("Permanent Account");
                            }else{
                                int interval = getTodayInterval(ao.getExpiration());
                                tv_accountType.setText("Temporary Account: " + interval + " days!");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

}
