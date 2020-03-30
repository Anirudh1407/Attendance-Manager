package com.example.attendance;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Attendance extends Fragment {

    private Activity refActivity;
    private View parentHolder;
    private ArrayList<Subject> newArrayList,fullArrayList,deletedArrayList;
    private Button addSubButton;
    private AsyncLoader asyncLoader;
     private SubjectListAdapter adapter;
    private RecyclerView subjectListRecyclerView;

    /////////Initiates loader task , functionality for button add subject when clicked/////////////////////////

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        refActivity = getActivity();
        parentHolder = inflater.inflate(R.layout.fragment_attendance,container,false);
        subjectListRecyclerView = parentHolder.findViewById(R.id.subjectList);
        asyncLoader = new AsyncLoader();
        newArrayList = new ArrayList<>();
        fullArrayList = new ArrayList<>();
        deletedArrayList = new ArrayList<>();
        subjectListRecyclerView.setLayoutManager(new LinearLayoutManager(refActivity));
        asyncLoader.execute();
        addSubButton = parentHolder.findViewById(R.id.addSubjectButton);
        addSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(refActivity);
                editText.setSingleLine(true);
                AlertDialog.Builder alert = new AlertDialog.Builder(refActivity);
                alert.setMessage("Enter name of subject");
                alert.setTitle("Add subject");
                alert.setView(editText);
                alert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        boolean flag = true;
                        Subject s = new Subject();
                        String subjectName = editText.getText().toString();
                        for(Subject sub:fullArrayList){
                            if(sub.getName().equals(subjectName)){
                                Toast.makeText(refActivity, "Name already exists", Toast.LENGTH_SHORT).show();
                                flag = false;
                                break;
                            }
                        }
                        if(flag) {
                            if (subjectName.equals("")) {
                                Toast.makeText(refActivity, "No subject name entered", Toast.LENGTH_SHORT).show();
                            } else if (subjectName.length() >    10) {
                                Toast.makeText(refActivity, "Subject names with length greater than 10 not allowed", Toast.LENGTH_SHORT).show();
                            } else {
                                s.setName(subjectName);
                                Toast.makeText(refActivity, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                                s.setCa(0);
                                s.setCd(0);
                                s.setChanged(false);
                                newArrayList.add(s);
                                fullArrayList.add(s);
                            }
                        }
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.setCancelable(false);
                alert.show();
            }
        });
        adapter = new SubjectListAdapter(refActivity, fullArrayList , deletedArrayList);
        subjectListRecyclerView.setAdapter(adapter);
        subjectListRecyclerView.addItemDecoration(new DividerItemDecoration(subjectListRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        return  parentHolder;
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



    ///////////This task loads data from the db/////////////////////////////////

     class AsyncLoader extends AsyncTask<Void,Void,ArrayList<Subject>> {
        private Cursor cursor;
        @Override
        protected ArrayList<Subject> doInBackground(Void... voids) {
            SQLiteDatabase database = refActivity.openOrCreateDatabase("SUBJECT",Context.MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Subject (name VARCHAR,ca INT,cd INT)");
            cursor = database.rawQuery("SELECT * FROM Subject",null);
            int nameIndex = cursor.getColumnIndex("name");
            int ca = cursor.getColumnIndex("ca");
            int cd = cursor.getColumnIndex("cd");
            if(cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    Subject s = new Subject();
                    s.setName(cursor.getString(nameIndex));
                    s.setCa(Integer.parseInt(cursor.getString(ca)));
                    s.setCd(Integer.parseInt(cursor.getString(cd)));
                    fullArrayList.add(s);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
            return fullArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<Subject> subjects) {
            adapter = new SubjectListAdapter(refActivity,subjects , deletedArrayList);
            subjectListRecyclerView.setAdapter(adapter);
            super.onPostExecute(subjects);
        }
    }

    ////////////This task stores new data in the db///////////////////////////////////////

    public class AsyncStore extends AsyncTask<ArrayList<Subject>,Void,Void> {

        @SafeVarargs
        @Override
        protected final Void doInBackground(ArrayList<Subject>... arrayLists) {
            SQLiteDatabase database = refActivity.openOrCreateDatabase("SUBJECT",Context.MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS Subject (name VARCHAR,ca INT,cd INT)");
            for(Subject s:arrayLists[0]) {
                String name = s.getName();
                int ca = s.getCa();
                int cd = s.getCd();
                String string = "INSERT INTO Subject (name,ca,cd) VALUES ('" + name + "'," + ca + "," + cd + ")";
                database.execSQL(string);
            }
            for(Subject s:arrayLists[1]){
                if(s.isChanged()){
                    database.execSQL("UPDATE Subject SET ca = " + s.getCa() + ",cd = " + s.getCd() + " WHERE name = '" + s.getName() + "'");
                    s.setChanged(false);
                }
            }
            for(Subject s:deletedArrayList){
                database.execSQL("DELETE FROM Subject WHERE name ='" + s.getName() + "'");
            }
            deletedArrayList.clear();
            database.close();
            return null;
        }
    }


    ///////////Initiates store task ie store to db , when activity is changed////////////////////////////////////

    @Override
    public void onPause() {
        super.onPause();
        AsyncStore asyncStore = new AsyncStore();
        asyncStore.execute(newArrayList,fullArrayList);
    }

}
