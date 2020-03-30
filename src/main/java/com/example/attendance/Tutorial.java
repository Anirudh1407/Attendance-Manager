package com.example.attendance;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class Tutorial extends AppCompatActivity {

    TextView tutorialTextView;
    int counter = 0;
    boolean setPercent;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        b = findViewById(R.id.nextButton);
        tutorialTextView = findViewById(R.id.tutorialTextView);
        counter = getIntent().getIntExtra("counter", 0);
         setPercent = getIntent().getBooleanExtra("setPercent",true);
        Log.i("setPercent:",String.valueOf(setPercent));
        if(counter==0)
            tutorialTextView.setText("To add a subject\nclick on the \n 'Add Subject' button");
        else{
            sharedStore();
        }

    }

    @SuppressLint("SetTextI18n")
    void sharedStore(){
        b.setText("SET PERCENTAGE");
        String set = "Set the minimum percentage";
        tutorialTextView.setText(set);
        final EditText editText = new EditText(this);
        editText.setSingleLine(true);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Enter minimum percentage");
        alert.setTitle("Minimum percentage");
        alert.setView(editText);
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String per;
                per = editText.getText().toString();
                if(per.equals("")){
                    Toast.makeText(Tutorial.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    counter--;
                }
                else {
                    Subject.setMinPer(Integer.parseInt(per));
                    SharedPreferences sharedPreferences = getSharedPreferences("Preferences", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("MinPer", Subject.getMinPer());
                    editor.putBoolean("Tutorial",false);
                    editor.apply();
                    startActivity(new Intent(Tutorial.this, MainActivity.class));
                }
            }
        })
                .setCancelable(false)
                .show();
    }

    public void next(View view) {
        counter++;
        Toast.makeText(this, String.valueOf(counter), Toast.LENGTH_SHORT).show();
        if (counter == 1) {
            tutorialTextView.setText("To update a subject\nclick on the the subject");
        } else if (counter == 2) {
            tutorialTextView.setText("To delete a subject\nlong press on the\n subject name");
        } else if (counter == 3) {
            if (setPercent)
                sharedStore();
            else
                startActivity(new Intent(Tutorial.this, MainActivity.class));
        }

    }

    @Override
    public void onBackPressed() {
        //finish();
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

}
