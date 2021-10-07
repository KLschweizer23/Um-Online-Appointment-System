package com.example.umonlineappointmentsystem;

import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;

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
            int plusDays = -1;
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

    public void setTableDataForUser(TableLayout tableLayout, GoogleSignInAccount account){
        
    }

    public void setTableData(TableLayout tableLayout, String date){

    }

    public String getCurrentDate(int plusDays){
        LocalDate currentDate = LocalDate.now().plusDays(plusDays);
        return currentDate.toString();
    }

}
