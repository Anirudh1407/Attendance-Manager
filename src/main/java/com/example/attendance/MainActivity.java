package com.example.attendance;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.attendance_calculator:
                    Attendance fragment1 = null;
                    fragment1 = new Attendance();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,fragment1).commit();
                    break;
                case R.id.hypo_calculator:
                    HypoCalculator fragment = null;
                    fragment = new HypoCalculator();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout,fragment).commit();
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigation =  findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Context context = getApplicationContext();
        SharedPreferences sharedPreferences = getSharedPreferences("Preferences",MODE_PRIVATE);
        boolean firstTime = sharedPreferences.getBoolean("Tutorial",true);
        int minPer = sharedPreferences.getInt("MinPer",0);
        Subject.setMinPer(minPer);
        if (firstTime) {
            startActivity(new Intent(this,Tutorial.class));
        }
        else{
            navigation.setSelectedItemId(R.id.attendance_calculator);
        }
    }

    @Override
    public void onBackPressed() {
        Context context = getApplicationContext();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}
