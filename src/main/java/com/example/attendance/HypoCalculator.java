package com.example.attendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class HypoCalculator extends Fragment {
    Activity refActivity;
    View parentHolder;
    EditText ca,cd;
    Button checkButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        refActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_hypo_calculator,container,false);
        ca = parentHolder.findViewById(R.id.caEditText);
        cd = parentHolder.findViewById(R.id.cdEditText);
        checkButton = parentHolder.findViewById(R.id.checkButton);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        refActivity.getSystemService(INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(refActivity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                String classesAttended = ca.getText().toString();
                String classesDone = cd.getText().toString();
                if(!classesAttended.equals("") && !classesDone.equals("")){
                    int ca = Integer.parseInt(classesAttended);
                    int cd = Integer.parseInt(classesDone);
                    if(ca>cd){
                        Toast.makeText(refActivity, "Invalid Input", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        int percent = ca * 100 / cd;
                        Toast.makeText(refActivity, String.valueOf(percent) + "%", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(refActivity, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return parentHolder;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.settings,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()){
            case R.id.tutorial:
                intent = new Intent(refActivity,Tutorial.class);
                intent.putExtra("setPercent",false);
                startActivity(intent);
                break;
            case R.id.minPercentSet:
                intent = new Intent(refActivity,Tutorial.class);
                intent.putExtra("counter",2);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



}
